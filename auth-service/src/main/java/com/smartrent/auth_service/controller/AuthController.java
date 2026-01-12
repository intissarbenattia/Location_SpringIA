package com.smartrent.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartrent.auth_service.entities.AppUser;
import com.smartrent.auth_service.model.AuthenticationRequest;
import com.smartrent.auth_service.model.AuthenticationResponse;
import com.smartrent.auth_service.repository.UserRepository;
import com.smartrent.auth_service.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtService jwtService,
                         UserDetailsService userDetailsService,
                         UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        System.out.println("\n========================================");
        System.out.println("🔐 TENTATIVE DE CONNEXION");
        System.out.println("========================================");
        System.out.println("📧 Username : " + request.getUsername());
        System.out.println("⏰ Timestamp : " + new java.util.Date());
        
        try {
            System.out.println("🔍 Validation des identifiants...");
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            System.out.println("✅ Identifiants valides !");
            
            System.out.println("📋 Chargement des détails utilisateur...");
            final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
            System.out.println("✅ Utilisateur chargé : " + user.getUsername());
            
            System.out.println("🎫 Génération du token JWT...");
            final String token = jwtService.generateToken(user);
            System.out.println("✅ Token généré avec succès");
            
            AppUser appUser = userRepository.findByUsername(request.getUsername());
            
            AuthenticationResponse response = AuthenticationResponse.builder()
                .token(token)
                .username(appUser.getUsername())
                .email(appUser.getEmail())
                .build();
            
            System.out.println("========================================");
            System.out.println("✅ CONNEXION RÉUSSIE !");
            System.out.println("========================================\n");
            
            return ResponseEntity.ok(response);
                
        } catch (BadCredentialsException e) {
            System.err.println("❌ ERREUR : Identifiants incorrects");
            System.err.println("========================================\n");
            return ResponseEntity.status(401)
                .body("Identifiants incorrects");
                
        } catch (Exception e) {
            System.err.println("❌ ERREUR SYSTÈME : " + e.getClass().getSimpleName());
            System.err.println("📝 Message : " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");
            return ResponseEntity.status(500)
                .body("Erreur serveur : " + e.getMessage());
        }
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("\n🔍 VALIDATION DE TOKEN");
        
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                System.err.println("❌ Token manquant ou format incorrect");
                return ResponseEntity.badRequest().body("Token manquant ou malformé");
            }
            
            String token = authorizationHeader.substring(7);
            System.out.println("📋 Token extrait (premiers caractères) : " + token.substring(0, Math.min(20, token.length())) + "...");
            
            String username = jwtService.extractUsername(token);
            System.out.println("👤 Username extrait : " + username);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(token, userDetails)) {
                System.out.println("✅ Token valide");
                return ResponseEntity.ok("Token valide pour l'utilisateur : " + username);
            } else {
                System.err.println("❌ Token invalide ou expiré");
                return ResponseEntity.status(401).body("Token invalide ou expiré");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur de validation : " + e.getMessage());
            return ResponseEntity.status(401).body("Erreur de validation : " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running! 🚀");
    }
}