package com.smartrent.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        System.out.println("🔍 JwtAuthenticationFilter - Path: " + path);
        
        if (shouldNotFilter(path)) {
            System.out.println("   ✅ Endpoint PUBLIC - Filtre JWT ignoré");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("   🔐 Endpoint PROTÉGÉ - Validation JWT requise");

        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("   ⚠️  Pas de token Bearer trouvé");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            System.out.println("   📋 Token extrait: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");
            
            final String username = jwtService.extractUsername(jwt);
            System.out.println("   👤 Username extrait: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    System.out.println("   ✅ Token VALIDE - Authentification OK");
                    
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("   ❌ Token INVALIDE ou EXPIRÉ");
                }
            }
        } catch (Exception e) {
            System.err.println("   ❌ Erreur lors de la validation du token JWT : " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
    
    private boolean shouldNotFilter(String path) {
        return path.startsWith("/api/auth/") || 
               path.startsWith("/v3/api-docs") || 
               path.startsWith("/swagger-ui") ||
               path.equals("/swagger-ui.html");
    }
}