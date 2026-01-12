package com.smartrent.reservation_service.dto;

import java.time.LocalDate;

/**
 * DTO enrichi avec les détails de la location
 */
public class ReservationDetailDTO {
    
    private Long reservationId;
    private String clientNom;
    private String clientEmail;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private Double prixTotal;
    
    // Détails de la location
    private Long locationId;
    private String locationNom;
    private String locationType;
    private String locationAdresse;
    private Double locationPrixParJour;
    private Boolean locationDisponible;
    private String locationDescription;
    
    public ReservationDetailDTO() {}
    
    public ReservationDetailDTO(Long reservationId, String clientNom, String clientEmail,
                               LocalDate dateDebut, LocalDate dateFin, String statut, Double prixTotal,
                               Long locationId, String locationNom, String locationType,
                               String locationAdresse, Double locationPrixParJour,
                               Boolean locationDisponible, String locationDescription) {
        this.reservationId = reservationId;
        this.clientNom = clientNom;
        this.clientEmail = clientEmail;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.prixTotal = prixTotal;
        this.locationId = locationId;
        this.locationNom = locationNom;
        this.locationType = locationType;
        this.locationAdresse = locationAdresse;
        this.locationPrixParJour = locationPrixParJour;
        this.locationDisponible = locationDisponible;
        this.locationDescription = locationDescription;
    }
    
    // Getters et Setters
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }
    
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    
    public String getLocationNom() { return locationNom; }
    public void setLocationNom(String locationNom) { this.locationNom = locationNom; }
    
    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }
    
    public String getLocationAdresse() { return locationAdresse; }
    public void setLocationAdresse(String locationAdresse) { this.locationAdresse = locationAdresse; }
    
    public Double getLocationPrixParJour() { return locationPrixParJour; }
    public void setLocationPrixParJour(Double locationPrixParJour) { this.locationPrixParJour = locationPrixParJour; }
    
    public Boolean getLocationDisponible() { return locationDisponible; }
    public void setLocationDisponible(Boolean locationDisponible) { this.locationDisponible = locationDisponible; }
    
    public String getLocationDescription() { return locationDescription; }
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }
    
    @Override
    public String toString() {
        return "ReservationDetailDTO{" +
                "reservationId=" + reservationId +
                ", clientNom='" + clientNom + '\'' +
                ", statut='" + statut + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", locationNom='" + locationNom + '\'' +
                ", locationType='" + locationType + '\'' +
                '}';
    }
}
