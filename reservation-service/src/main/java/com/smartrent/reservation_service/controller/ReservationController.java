package com.smartrent.reservation_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartrent.reservation_service.entities.Reservation;
import com.smartrent.reservation_service.service.ReservationService;

/**
 * Contrôleur REST pour la gestion des réservations
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    
    /**
     * Crée une nouvelle réservation
     * POST /reservations
     */
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation created = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Récupère toutes les réservations
     * GET /reservations
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAll() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Récupère une réservation par son ID
     * GET /reservations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }
    
    /**
     * Récupère les réservations par location
     * GET /reservations/location/{locationId}
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Reservation>> getByLocation(@PathVariable Long locationId) {
        List<Reservation> reservations = reservationService.getReservationsByLocation(locationId);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Récupère les réservations par client
     * GET /reservations/client/{email}
     */
    @GetMapping("/client/{email}")
    public ResponseEntity<List<Reservation>> getByClient(@PathVariable String email) {
        List<Reservation> reservations = reservationService.getReservationsByClient(email);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Récupère les réservations par statut
     * GET /reservations/statut/{statut}
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Reservation>> getByStatut(@PathVariable String statut) {
        List<Reservation> reservations = reservationService.getReservationsByStatut(statut);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Met à jour une réservation
     * PUT /reservations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        Reservation updated = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Confirme une réservation
     * POST /reservations/{id}/confirm
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirm(@PathVariable Long id) {
        Reservation confirmed = reservationService.confirmReservation(id);
        return ResponseEntity.ok(confirmed);
    }
    
    /**
     * Annule une réservation
     * POST /reservations/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        Reservation cancelled = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelled);
    }
    
    /**
     * Supprime une réservation
     * DELETE /reservations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}