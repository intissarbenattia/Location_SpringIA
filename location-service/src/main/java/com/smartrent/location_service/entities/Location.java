package com.smartrent.location_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String nom;
    private String adresse;

    @Column(name = "prix_par_jour")
    private Double prixParJour;

    private Boolean disponible;

    @Column(length = 1000)
    private String description;
















    
    // Constructeurs
    public Location() {
    }

    public Location(Long id, String type, String nom, String adresse, 
                    Double prixParJour, Boolean disponible, String description) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.adresse = adresse;
        this.prixParJour = prixParJour;
        this.disponible = disponible;
        this.description = description;
    }

    // Getters
    public Long getId() { return id; }
    public String getType() { return type; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public Double getPrixParJour() { return prixParJour; }
    public Boolean getDisponible() { return disponible; }
    public String getDescription() { return description; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setPrixParJour(Double prixParJour) { this.prixParJour = prixParJour; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public void setDescription(String description) { this.description = description; }

    // Builder Pattern
    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    public static class LocationBuilder {
        private Long id;
        private String type;
        private String nom;
        private String adresse;
        private Double prixParJour;
        private Boolean disponible;
        private String description;

        public LocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocationBuilder type(String type) {
            this.type = type;
            return this;
        }

        public LocationBuilder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public LocationBuilder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public LocationBuilder prixParJour(Double prixParJour) {
            this.prixParJour = prixParJour;
            return this;
        }

        public LocationBuilder disponible(Boolean disponible) {
            this.disponible = disponible;
            return this;
        }

        public LocationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Location build() {
            return new Location(id, type, nom, adresse, prixParJour, disponible, description);
        }
    }
}