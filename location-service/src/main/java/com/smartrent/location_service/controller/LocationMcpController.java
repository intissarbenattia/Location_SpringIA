package com.smartrent.location_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.service.LocationService;

@RestController
@RequestMapping("/mcp")
public class LocationMcpController {

    private final LocationService locationService;

    public LocationMcpController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Liste des outils disponibles
     */
    @GetMapping("/tools")
    public Map<String, Object> listTools() {
        return Map.of("tools", List.of(
            Map.of(
                "name", "rechercher_locations",
                "description", "Récupère toutes les locations",
                "parameters", Map.of()
            ),
            Map.of(
                "name", "rechercher_locations_disponibles",
                "description", "Récupère les locations disponibles",
                "parameters", Map.of()
            ),
            Map.of(
                "name", "rechercher_par_type",
                "description", "Recherche par type",
                "parameters", Map.of(
                    "type", Map.of("type", "string", "required", true)
                )
            ),
            Map.of(
                "name", "rechercher_disponibles_par_type",
                "description", "Recherche des locations disponibles par type",
                "parameters", Map.of(
                    "type", Map.of("type", "string", "required", true)
                )
            ),
            Map.of(
                "name", "details_location",
                "description", "Retourne les détails d'une location",
                "parameters", Map.of(
                    "id", Map.of("type", "integer", "required", true)
                )
            ),
            Map.of(
                "name", "calculer_prix_location",
                "description", "Calcule le prix",
                "parameters", Map.of(
                    "locationId", Map.of("type", "integer", "required", true),
                    "nombreJours", Map.of("type", "integer", "required", true)
                )
            )
        ));
    }

    /**
     * Exécute un outil
     */
    @PostMapping("/execute")
    public Map<String, Object> executeTool(@RequestBody Map<String, Object> request) {
        String tool = (String) request.get("tool");
        Map<String, Object> params = 
            (Map<String, Object>) request.getOrDefault("parameters", new HashMap<>());

        try {
            Object result = switch (tool) {
                case "rechercher_locations" ->
                    locationService.getAllLocations();

                case "rechercher_locations_disponibles" ->
                    locationService.getAvailableLocations();

                case "rechercher_par_type" -> {
                    String type = (String) params.get("type");
                    yield locationService.getLocationsByType(type);
                }

                case "rechercher_disponibles_par_type" -> {
                    String type = (String) params.get("type");
                    yield locationService.getAvailableLocationsByType(type);
                }

                case "details_location" -> {
                    Long id = Long.valueOf(params.get("id").toString());
                    yield locationService.getLocationById(id);
                }

                case "calculer_prix_location" -> {
                    Long locationId =
                        Long.valueOf(params.get("locationId").toString());
                    Integer jours =
                        Integer.valueOf(params.get("nombreJours").toString());

                    Location loc =
                        locationService.getLocationById(locationId);

                    yield loc != null ? loc.getPrixParJour() * jours : null;
                }

                default ->
                    throw new IllegalArgumentException("Outil inconnu: " + tool);
            };

            return Map.of(
                "success", true,
                "result", result
            );

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }
}