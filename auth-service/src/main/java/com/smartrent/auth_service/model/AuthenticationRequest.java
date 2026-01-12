// ========== AuthenticationRequest.java ==========
package com.smartrent.auth_service.model;

public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest() {}
    
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }













    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private String username;
        private String password;
        
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        
        public AuthenticationRequest build() {
            return new AuthenticationRequest(username, password);
        }
    }
}