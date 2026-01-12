package com.smartrent.reservation_service.repository;

import com.smartrent.reservation_service.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByLocationId(Long locationId);
    
    List<Reservation> findByClientEmail(String clientEmail);
    
    List<Reservation> findByStatut(String statut);
}