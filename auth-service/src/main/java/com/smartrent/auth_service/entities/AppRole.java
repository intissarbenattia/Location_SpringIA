package com.smartrent.auth_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_roles")
public class AppRole {
    @Id
    private String nom;

    public AppRole() {}
    
    public AppRole(String nom) {
        this.nom = nom;
    }











    

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private String nom;
        
        public Builder nom(String nom) { this.nom = nom; return this; }
        
        public AppRole build() {
            return new AppRole(nom);
        }
    }
}