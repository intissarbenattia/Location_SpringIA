package com.smartrent.reservation_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client pour appeler le Location Service et récupérer les détails des locations
 */
@Component
public class LocationServiceClient {
    
    @Autowired(required = false)
    private RestTemplate restTemplate;
    
    private static final String LOCATION_SERVICE_URL = "http://localhost:9091/api/locations";
    
    /**
     * Récupère les détails d'une location par son ID
     */
    public Map<String, Object> getLocationDetails(Long locationId) {
        try {
            if (restTemplate == null) {
                // Si RestTemplate n'est pas disponible, retourner null
                return null;
            }
            
            String url = LOCATION_SERVICE_URL + "/" + locationId;
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            System.err.println("❌ Erreur lors de l'appel au Location Service: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Erreur inattendue: " + e.getMessage());
            return null;
        }
    }
}
