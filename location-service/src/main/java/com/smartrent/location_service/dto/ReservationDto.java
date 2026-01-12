package com.smartrent.location_service.dto;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private Long locationId;
    private String clientNom;
    private String clientEmail;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private Double prixTotal;
}
