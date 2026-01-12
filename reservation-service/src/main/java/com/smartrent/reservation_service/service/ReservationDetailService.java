package com.smartrent.reservation_service.service;

import com.smartrent.reservation_service.client.LocationServiceClient;
import com.smartrent.reservation_service.dto.ReservationDetailDTO;
import com.smartrent.reservation_service.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service pour enrichir les réservations avec les détails des locations
 */
@Service
public class ReservationDetailService {
    
    @Autowired(required = false)
    private LocationServiceClient locationServiceClient;
    
    /**
     * Convertit une Reservation en ReservationDetailDTO en enrichissant avec les détails de la location
     */
    public ReservationDetailDTO enrichReservation(Reservation reservation) {
        ReservationDetailDTO dto = new ReservationDetailDTO();
        
        // Données de la réservation
        dto.setReservationId(reservation.getId());
        dto.setClientNom(reservation.getClientNom());
        dto.setClientEmail(reservation.getClientEmail());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setStatut(reservation.getStatut());
        dto.setPrixTotal(reservation.getPrixTotal());
        dto.setLocationId(reservation.getLocationId());
        
        // Récupérer les détails de la location
        if (locationServiceClient != null) {
            try {
                Map<String, Object> locationDetails = locationServiceClient.getLocationDetails(reservation.getLocationId());
                if (locationDetails != null) {
                    dto.setLocationNom((String) locationDetails.get("nom"));
                    dto.setLocationType((String) locationDetails.get("type"));
                    dto.setLocationAdresse((String) locationDetails.get("adresse"));
                    dto.setLocationPrixParJour((Double) locationDetails.get("prixParJour"));
                    dto.setLocationDisponible((Boolean) locationDetails.get("disponible"));
                    dto.setLocationDescription((String) locationDetails.get("description"));
                }
            } catch (Exception e) {
                System.err.println("⚠️ Impossible de récupérer les détails de la location " + reservation.getLocationId() + ": " + e.getMessage());
            }
        }
        
        return dto;
    }
    
    /**
     * Enrichit une liste de réservations
     */
    public List<ReservationDetailDTO> enrichReservations(List<Reservation> reservations) {
        List<ReservationDetailDTO> enrichedList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            enrichedList.add(enrichReservation(reservation));
        }
        return enrichedList;
    }
}
