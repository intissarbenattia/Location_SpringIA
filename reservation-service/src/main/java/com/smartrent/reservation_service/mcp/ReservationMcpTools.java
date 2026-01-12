package com.smartrent.reservation_service.mcp;

import com.smartrent.reservation_service.dto.ReservationDetailDTO;
import com.smartrent.reservation_service.service.ReservationDetailService;
import com.smartrent.reservation_service.service.ReservationService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 🛠️ MCP Tools pour le service Reservation
 * Tous les outils retournent des données enrichies avec les détails complets
 */
@Component
public class ReservationMcpTools {
    
    private final ReservationService reservationService;
    private final ReservationDetailService reservationDetailService;
    
    public ReservationMcpTools(ReservationService reservationService, ReservationDetailService reservationDetailService) {
        this.reservationService = reservationService;
        this.reservationDetailService = reservationDetailService;
    }
    
    @Tool(
        name = "rechercher_reservations",
        description = "Récupère TOUTES les réservations enregistrées dans la base de données"
    )
    public List<ReservationDetailDTO> rechercherReservations() {
        System.out.println("🔍 Tool appelé: rechercher_reservations");
        var reservations = reservationService.getAllReservations();
        System.out.println("✅ " + reservations.size() + " réservations trouvées au total");
        return reservationDetailService.enrichReservations(reservations);
    }
    
    @Tool(
        name = "rechercher_reservations_confirmees",
        description = "Récupère UNIQUEMENT les réservations avec le statut CONFIRMÉE"
    )
    public List<ReservationDetailDTO> rechercherReservationsConfirmees() {
        System.out.println("🔍 Tool appelé: rechercher_reservations_confirmees");
        var reservations = reservationService.getReservationsByStatut("CONFIRMÉE");
        System.out.println("✅ " + reservations.size() + " réservations confirmées trouvées");
        return reservationDetailService.enrichReservations(reservations);
    }
    
    @Tool(
        name = "rechercher_reservations_client",
        description = "Recherche TOUTES les réservations d'un client spécifique par son adresse email"
    )
    public List<ReservationDetailDTO> rechercherReservationsClient(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("❌ Erreur: Le paramètre 'email' est null ou vide");
            return List.of();
        }
        System.out.println("🔍 Tool appelé: rechercher_reservations_client email=" + email);
        var reservations = reservationService.getReservationsByClient(email);
        System.out.println("✅ " + reservations.size() + " réservations trouvées pour " + email);
        return reservationDetailService.enrichReservations(reservations);
    }
    
    @Tool(
        name = "details_reservation",
        description = "Obtient les DÉTAILS COMPLETS d'une réservation spécifique incluant client, dates, location et prix"
    )
    public ReservationDetailDTO detailsReservation(Long reservationId) {
        System.out.println("🔍 Tool appelé: details_reservation id=" + reservationId);
        var reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            System.err.println("❌ Réservation " + reservationId + " non trouvée");
            throw new RuntimeException("Réservation non trouvée: " + reservationId);
        }
        System.out.println("✅ Réservation trouvée pour client: " + reservation.getClientNom());
        return reservationDetailService.enrichReservation(reservation);
    }
    
    @Tool(
        name = "reservations_par_location",
        description = "Récupère TOUTES les réservations associées à une location spécifique par son ID"
    )
    public List<ReservationDetailDTO> reservationsParLocation(Long locationId) {
        System.out.println("🔍 Tool appelé: reservations_par_location locationId=" + locationId);
        var reservations = reservationService.getReservationsByLocation(locationId);
        System.out.println("✅ " + reservations.size() + " réservations trouvées pour location " + locationId);
        return reservationDetailService.enrichReservations(reservations);
    }
    
    @Tool(
        name = "rechercher_reservations_par_statut",
        description = "Recherche des réservations filtrées par statut (EN_ATTENTE, CONFIRMÉE, ANNULÉE)"
    )
    public List<ReservationDetailDTO> rechercherReservationsParStatut(String statut) {
        if (statut == null || statut.trim().isEmpty()) {
            System.err.println("❌ Erreur: Le paramètre 'statut' est null ou vide");
            return List.of();
        }
        String statutUpper = statut.toUpperCase().trim();
        System.out.println("🔍 Tool appelé: rechercher_reservations_par_statut statut=" + statutUpper);
        var reservations = reservationService.getReservationsByStatut(statutUpper);
        System.out.println("✅ " + reservations.size() + " réservations trouvées avec statut " + statutUpper);
        return reservationDetailService.enrichReservations(reservations);
    }
}
