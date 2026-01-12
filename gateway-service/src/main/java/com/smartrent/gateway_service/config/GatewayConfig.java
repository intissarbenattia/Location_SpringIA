package com.smartrent.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;

/**
 * Configuration des routes du Gateway
 * Assure que les paths /api/* sont forwarded sans modification
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route Auth Service - NO STRIP
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://auth-service"))

                // Route Location Service - NO STRIP
                .route("location-service", r -> r
                        .path("/api/locations/**")
                        .uri("lb://location-service"))

                // Route Agent IA Service - NO STRIP
                .route("agent-ia-service", r -> r
                        .path("/api/agent/**")
                        .uri("lb://agent-ia-service"))

                // Route Reservation Service - NO STRIP
                .route("reservation-service", r -> r
                        .path("/api/reservations/**")
                        .uri("lb://reservation-service"))

                .build();
    }
}
