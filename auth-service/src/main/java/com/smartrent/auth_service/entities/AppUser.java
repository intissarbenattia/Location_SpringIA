// ========== AppUser.java ==========
package com.smartrent.auth_service.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    private String id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_nom")
    )
    private List<AppRole> roles;














    
    // Constructeurs
    public AppUser() {}
    
    public AppUser(String id, String username, String password, String email, List<AppRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<AppRole> getRoles() { return roles; }
    public void setRoles(List<AppRole> roles) { this.roles = roles; }

    // Builder pattern
    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private String id;
        private String username;
        private String password;
        private String email;
        private List<AppRole> roles;
        
        public Builder id(String id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder roles(List<AppRole> roles) { this.roles = roles; return this; }
        
        public AppUser build() {
            return new AppUser(id, username, password, email, roles);
        }
    }
}