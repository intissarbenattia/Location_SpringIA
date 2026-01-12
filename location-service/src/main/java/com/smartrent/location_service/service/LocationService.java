package com.smartrent.location_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.repository.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location introuvable avec l'ID: " + id));
    }

    public List<Location> getLocationsByType(String type) {
        return locationRepository.findByType(type.toUpperCase());
    }

    public List<Location> getAvailableLocations() {
        return locationRepository.findByDisponible(true);
    }

    public List<Location> getAvailableLocationsByType(String type) {
        return locationRepository.findByTypeAndDisponible(type.toUpperCase(), true);
    }

    public Location updateLocation(Long id, Location newLocation) {
        Location location = getLocationById(id);
        location.setType(newLocation.getType());
        location.setNom(newLocation.getNom());
        location.setAdresse(newLocation.getAdresse());
        location.setPrixParJour(newLocation.getPrixParJour());
        location.setDisponible(newLocation.getDisponible());
        location.setDescription(newLocation.getDescription());
        return locationRepository.save(location);
    }

    public Location setDisponibilite(Long id, Boolean disponible) {
        Location location = getLocationById(id);
        location.setDisponible(disponible);
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}