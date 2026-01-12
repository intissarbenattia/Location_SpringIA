package com.smartrent.location_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.service.LocationService;

/**
 * Contrôleur REST pour la gestion des locations
 */
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Crée une nouvelle location
     * POST /locations
     */
    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        Location created = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Récupère toutes les locations
     * GET /locations
     */
    @GetMapping
    public ResponseEntity<List<Location>> getAll() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Récupère une location par son ID
     * GET /locations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    /**
     * Récupère les locations par type
     * GET /locations/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> getByType(@PathVariable String type) {
        List<Location> locations = locationService.getLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    /**
     * Récupère les locations disponibles
     * GET /locations/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<Location>> getAvailable() {
        List<Location> locations = locationService.getAvailableLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Récupère les locations disponibles par type
     * GET /locations/type/{type}/available
     */
    @GetMapping("/type/{type}/available")
    public ResponseEntity<List<Location>> getAvailableByType(@PathVariable String type) {
        List<Location> locations = locationService.getAvailableLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    /**
     * Met à jour une location
     * PUT /locations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Location> update(
            @PathVariable Long id,
            @RequestBody Location location) {
        Location updated = locationService.updateLocation(id, location);
        return ResponseEntity.ok(updated);
    }

    /**
     * Modifie la disponibilité d'une location
     * PATCH /locations/{id}/disponible?disponible=true
     */
    @PatchMapping("/{id}/disponible")
    public ResponseEntity<Location> setDisponibilite(
            @PathVariable Long id,
            @RequestParam Boolean disponible) {
        Location updated = locationService.setDisponibilite(id, disponible);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime une location
     * DELETE /locations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}