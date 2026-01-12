package com.smartrent.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;

import java.util.Map;
@Configuration
@ConfigurationProperties(prefix = "mcp")
@Data
public class McpClientConfig {

    private Map<String, ServiceConfig> services;

    public Map<String, ServiceConfig> getServices() {
        return services;
    }

    public void setServices(Map<String, ServiceConfig> services) {
        this.services = services;
    }

    @Data
    public static class ServiceConfig {
        private String url;
        private String endpoint;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    @Bean
    public WebClient.Builder webClientBuilder() {  // ❌ INUTILE - À SUPPRIMER
        return WebClient.builder();
    }
}