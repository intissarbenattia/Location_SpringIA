package com.smartrent.auth_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI pour Swagger UI.
 * Cette configuration active l'interface de documentation interactive
 * et configure le schéma d'authentification JWT.
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
            // Informations sur l'API
            .info(new Info()
                .title("SmartRent AI - Auth API")
                .version("1.0")
                .description("API d'authentification pour SmartRent AI"))
            
            // Ajouter le schéma de sécurité global
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            
            // Définir le schéma Bearer JWT
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            );
    }
}