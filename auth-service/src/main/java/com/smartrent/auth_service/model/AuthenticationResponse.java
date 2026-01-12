package com.smartrent.auth_service.model;

public class AuthenticationResponse {
    private String token;
    private String username;
    private String email;

    public AuthenticationResponse() {}
    
    public AuthenticationResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }















    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private String token;
        private String username;
        private String email;
        
        public Builder token(String token) { this.token = token; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder email(String email) { this.email = email; return this; }
        
        public AuthenticationResponse build() {
            return new AuthenticationResponse(token, username, email);
        }
    }
}