package com.smartrent.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration Spring Security - Auth Service
 * ⚠️ CORS est géré par la Gateway, pas ici !
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, 
                         AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("\n========================================");
        System.out.println("🔧 CONFIGURATION SPRING SECURITY - Auth Service");
        System.out.println("========================================");

        http
            // ⚠️ PAS de configuration CORS ici - la Gateway s'en charge !
            .cors(cors -> cors.disable())

            // CSRF désactivé pour REST API stateless
            .csrf(csrf -> csrf.disable())
            
            // Sessions STATELESS
            .sessionManagement(sess -> {
                System.out.println("✅ Sessions STATELESS configurées");
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            
            // Autorisations
            .authorizeHttpRequests(auth -> {
                System.out.println("✅ Configuration des autorisations :");
                System.out.println("   - /api/auth/** : PUBLIC");
                System.out.println("   - /v3/api-docs/** : PUBLIC");
                System.out.println("   - /swagger-ui/** : PUBLIC");
                System.out.println("   - Tout le reste : AUTHENTIFIÉ");
                
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated();
            })
            
            // Provider et filtre JWT
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ Filtre JWT ajouté AVANT UsernamePasswordAuthenticationFilter");
        System.out.println("✅ AnonymousAuthenticationFilter ACTIVÉ (par défaut)");
        System.out.println("⚠️  CORS géré par la Gateway uniquement");
        System.out.println("========================================\n");

        return http.build();
    }
}