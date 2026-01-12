package com.smartrent.reservation_service.service;

import com.smartrent.reservation_service.client.LocationFeignClient;
import com.smartrent.reservation_service.dto.LocationDto;
import com.smartrent.reservation_service.entities.Reservation;
import com.smartrent.reservation_service.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service de gestion des réservations
 */
@Service
@Transactional
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final LocationFeignClient locationFeignClient;
    
    public ReservationService(ReservationRepository reservationRepository, 
                             LocationFeignClient locationFeignClient) {
        this.reservationRepository = reservationRepository;
        this.locationFeignClient = locationFeignClient;
    }
    
    /**
     * Crée une nouvelle réservation
     */
    public Reservation createReservation(Reservation reservation) {
        // Vérifier que la location existe et calculer le prix
        try {
            LocationDto location = locationFeignClient.getLocationById(reservation.getLocationId());
            
            if (location == null || !location.getDisponible()) {
                throw new RuntimeException("Location non disponible: " + reservation.getLocationId());
            }
            
            // Calculer le prix total
            long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
            if (jours <= 0) {
                throw new RuntimeException("Les dates sont invalides");
            }
            
            reservation.setPrixTotal(location.getPrixParJour() * jours);
            reservation.setStatut("EN_ATTENTE");
            
            return reservationRepository.save(reservation);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la réservation: " + e.getMessage());
        }
    }
    
    /**
     * Récupère toutes les réservations
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    /**
     * Récupère une réservation par son ID
     */
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée: " + id));
    }
    
    /**
     * Récupère les réservations par location
     */
    public List<Reservation> getReservationsByLocation(Long locationId) {
        return reservationRepository.findByLocationId(locationId);
    }
    
    /**
     * Récupère les réservations par client
     */
    public List<Reservation> getReservationsByClient(String clientEmail) {
        return reservationRepository.findByClientEmail(clientEmail);
    }
    
    /**
     * Récupère les réservations par statut
     */
    public List<Reservation> getReservationsByStatut(String statut) {
        return reservationRepository.findByStatut(statut);
    }
    
    /**
     * Met à jour une réservation
     */
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existing = getReservationById(id);
        
        existing.setClientNom(reservation.getClientNom());
        existing.setClientEmail(reservation.getClientEmail());
        existing.setDateDebut(reservation.getDateDebut());
        existing.setDateFin(reservation.getDateFin());
        
        // Recalculer le prix si les dates ont changé
        try {
            LocationDto location = locationFeignClient.getLocationById(existing.getLocationId());
            long jours = ChronoUnit.DAYS.between(existing.getDateDebut(), existing.getDateFin());
            existing.setPrixTotal(location.getPrixParJour() * jours);
        } catch (Exception e) {
            // Garder l'ancien prix en cas d'erreur
        }
        
        return reservationRepository.save(existing);
    }
    
    /**
     * Confirme une réservation
     */
    public Reservation confirmReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservation.setStatut("CONFIRMEE");
        return reservationRepository.save(reservation);
    }
    
    /**
     * Annule une réservation
     */
    public Reservation cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservation.setStatut("ANNULEE");
        return reservationRepository.save(reservation);
    }
    
    /**
     * Supprime une réservation
     */
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Réservation non trouvée: " + id);
        }
        reservationRepository.deleteById(id);
    }
}