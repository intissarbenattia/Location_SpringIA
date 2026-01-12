package com.smartrent.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;








@Service
public class JwtService {
    
    // Clé secrète utilisée pour signer et vérifier les tokens
    @Value("${spring.app.secretkey}")
    private String secretKey;
    
    // Durée de validité du token en millisecondes
    @Value("${spring.app.expiration}")
    private long expiration;
    
    /**
     * Extrait le nom d'utilisateur (le "sujet" du token) à partir d'un token JWT.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    /**
     * Génère un nouveau token JWT pour un utilisateur authentifié.
     * Cette méthode est typiquement appelée après une connexion réussie.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Ajout des rôles (autorités) de l'utilisateur comme une revendication personnalisée
        // Cela permet de créer un token "autonome" : le serveur peut vérifier les autorisations
        // de l'utilisateur en se basant uniquement sur le contenu du token
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Valide un token JWT.
     * La validation se fait en deux étapes cruciales pour la sécurité :
     * 1. On vérifie que le nom d'utilisateur dans le token correspond à celui de l'utilisateur en cours.
     * 2. On s'assure que le token n'a pas expiré.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    // --- Méthodes privées utilitaires ---
    
    /**
     * Vérifie si un token a expiré en comparant sa date d'expiration avec la date actuelle.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Extrait la date d'expiration d'un token JWT.
     */
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    /**
     * Méthode centrale pour la création du token.
     * Elle utilise un "builder" pour assembler les différentes parties du JWT :
     * - Les revendications (claims), incluant les claims personnalisés et le sujet.
     * - La date d'émission (Issued At).
     * - La date d'expiration (Expiration).
     * - La signature avec l'algorithme et la clé secrète.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
    }
    
    /**
     * Extrait toutes les revendications (claims) d'un token.
     * Opération critique : avant d'extraire le corps (payload), la bibliothèque jjwt
     * va d'abord vérifier la signature du token en utilisant la clé secrète.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    /**
     * Génère la clé de signature à partir de la clé secrète.
     */
    private Key getSigningKey() {
        // Use the secret key directly as UTF-8 bytes for HMAC-SHA384
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}