package com.smartrent.reservation_service.dto;

/**
 * DTO pour recevoir les données de Location Service
 */
public class LocationDto {
    
    private Long id;
    private String type;
    private String nom;
    private String adresse;
    private Double prixParJour;
    private Boolean disponible;
    private String description;
    
    // Constructeurs
    public LocationDto() {
    }
    
    public LocationDto(Long id, String type, String nom, String adresse, 
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
    public Long getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public Double getPrixParJour() {
        return prixParJour;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public void setPrixParJour(Double prixParJour) {
        this.prixParJour = prixParJour;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}