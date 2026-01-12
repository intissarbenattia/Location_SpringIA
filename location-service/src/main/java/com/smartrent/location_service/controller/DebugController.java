package com.smartrent.location_service.controller;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final LocationService locationService;

    public DebugController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations/count")
    public Map<String, Object> getLocationsCount() {
        List<Location> allLocations = locationService.getAllLocations();
        List<Location> availableLocations = locationService.getAvailableLocations();
        
        return Map.of(
            "totalCount", allLocations.size(),
            "availableCount", availableLocations.size(),
            "allLocations", allLocations,
            "message", "✅ Debug Info - Toutes les " + allLocations.size() + " locations sont chargées"
        );
    }

    @GetMapping("/locations/by-type/APPARTEMENT")
    public Map<String, Object> getApartments() {
        List<Location> apartments = locationService.getLocationsByType("APPARTEMENT");
        return Map.of(
            "type", "APPARTEMENT",
            "count", apartments.size(),
            "locations", apartments
        );
    }

    @GetMapping("/locations/by-type/MAISON")
    public Map<String, Object> getHouses() {
        List<Location> houses = locationService.getLocationsByType("MAISON");
        return Map.of(
            "type", "MAISON",
            "count", houses.size(),
            "locations", houses
        );
    }

    @GetMapping("/locations/by-type/VOITURE")
    public Map<String, Object> getCars() {
        List<Location> cars = locationService.getLocationsByType("VOITURE");
        return Map.of(
            "type", "VOITURE",
            "count", cars.size(),
            "locations", cars
        );
    }
}
