package com.smartrent.reservation_service.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long locationId;
    
    @Column(nullable = false)
    private String clientNom;
    
    @Column(nullable = false)
    private String clientEmail;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @Column(nullable = false)
    private LocalDate dateFin;
    
    @Column(nullable = false)
    private String statut; // EN_ATTENTE, CONFIRMEE, ANNULEE
    
    private Double prixTotal;
    
    // Constructeurs
    public Reservation() {
    }
    
    public Reservation(Long id, Long locationId, String clientNom, String clientEmail, 
                      LocalDate dateDebut, LocalDate dateFin, String statut, Double prixTotal) {
        this.id = id;
        this.locationId = locationId;
        this.clientNom = clientNom;
        this.clientEmail = clientEmail;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.prixTotal = prixTotal;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getLocationId() {
        return locationId;
    }
    
    public String getClientNom() {
        return clientNom;
    }
    
    public String getClientEmail() {
        return clientEmail;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public Double getPrixTotal() {
        return prixTotal;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    
    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }
    
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }
    
    // Builder Pattern
    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }
    
    public static class ReservationBuilder {
        private Long id;
        private Long locationId;
        private String clientNom;
        private String clientEmail;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String statut;
        private Double prixTotal;
        
        public ReservationBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public ReservationBuilder locationId(Long locationId) {
            this.locationId = locationId;
            return this;
        }
        
        public ReservationBuilder clientNom(String clientNom) {
            this.clientNom = clientNom;
            return this;
        }
        
        public ReservationBuilder clientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }
        
        public ReservationBuilder dateDebut(LocalDate dateDebut) {
            this.dateDebut = dateDebut;
            return this;
        }
        
        public ReservationBuilder dateFin(LocalDate dateFin) {
            this.dateFin = dateFin;
            return this;
        }
        
        public ReservationBuilder statut(String statut) {
            this.statut = statut;
            return this;
        }
        
        public ReservationBuilder prixTotal(Double prixTotal) {
            this.prixTotal = prixTotal;
            return this;
        }
        
        public Reservation build() {
            return new Reservation(id, locationId, clientNom, clientEmail, 
                                 dateDebut, dateFin, statut, prixTotal);
        }
    }
}