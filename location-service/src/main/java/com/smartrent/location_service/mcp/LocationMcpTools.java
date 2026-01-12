package com.smartrent.location_service.mcp;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.service.LocationService;

@Component
public class LocationMcpTools {

    private final LocationService locationService;

    public LocationMcpTools(LocationService locationService) {
        this.locationService = locationService;
    }

    @Tool(
        name = "rechercher_locations",
        description = "Récupère TOUTES les locations disponibles dans la base de données"
    )
    public List<Location> rechercherLocations() {
        System.out.println("🔍 Tool: rechercher_locations appelé");
        List<Location> locations = locationService.getAllLocations();
        System.out.println("✅ " + locations.size() + " locations trouvées au total");
        return locations;
    }

    @Tool(
        name = "rechercher_locations_disponibles",
        description = "Récupère UNIQUEMENT les locations actuellement disponibles"
    )
    public List<Location> rechercherLocationsDisponibles() {
        System.out.println("🔍 Tool: rechercher_locations_disponibles appelé");
        List<Location> locations = locationService.getAvailableLocations();
        System.out.println("✅ " + locations.size() + " locations disponibles trouvées");
        return locations;
    }

    @Tool(
        name = "rechercher_par_type",
        description = "Recherche des locations par type exact (APPARTEMENT, MAISON, STUDIO, VOITURE, EQUIPEMENT)"
    )
    public List<Location> rechercherParType(String type) {
        if (type == null || type.trim().isEmpty()) {
            System.err.println("❌ Erreur: Le paramètre 'type' est null ou vide");
            return List.of();
        }
        String typeUpper = type.toUpperCase().trim();
        System.out.println("🔍 Tool: rechercher_par_type - type=" + typeUpper);
        List<Location> locations = locationService.getLocationsByType(typeUpper);
        System.out.println("✅ " + locations.size() + " locations de type " + typeUpper + " trouvées");
        return locations;
    }

    @Tool(
        name = "rechercher_appartements_disponibles",
        description = "Recherche les appartements disponibles immédiatement"
    )
    public List<Location> rechercherAppartementsDisponibles() {
        System.out.println("🔍 Tool: rechercher_appartements_disponibles appelé");
        List<Location> apartments = locationService.getAvailableLocationsByType("APPARTEMENT");
        System.out.println("✅ " + apartments.size() + " appartements disponibles trouvés");
        return apartments;
    }

    @Tool(
        name = "rechercher_maisons_disponibles",
        description = "Recherche les maisons disponibles pour une réservation"
    )
    public List<Location> rechercherMaisonsDisponibles() {
        System.out.println("🔍 Tool: rechercher_maisons_disponibles appelé");
        List<Location> houses = locationService.getAvailableLocationsByType("MAISON");
        System.out.println("✅ " + houses.size() + " maisons disponibles trouvées");
        return houses;
    }

    @Tool(
        name = "rechercher_studios_disponibles",
        description = "Recherche les studios disponibles pour une location courte durée"
    )
    public List<Location> rechercherStudiosDisponibles() {
        System.out.println("🔍 Tool: rechercher_studios_disponibles appelé");
        List<Location> studios = locationService.getAvailableLocationsByType("STUDIO");
        System.out.println("✅ " + studios.size() + " studios disponibles trouvés");
        return studios;
    }

    @Tool(
        name = "rechercher_voitures_disponibles",
        description = "Recherche les voitures de location disponibles"
    )
    public List<Location> rechercherVoituresDisponibles() {
        System.out.println("🔍 Tool: rechercher_voitures_disponibles appelé");
        List<Location> cars = locationService.getAvailableLocationsByType("VOITURE");
        System.out.println("✅ " + cars.size() + " voitures disponibles trouvées");
        return cars;
    }

    @Tool(
        name = "rechercher_disponibles_par_type",
        description = "Recherche des locations disponibles pour un type spécifique"
    )
    public List<Location> rechercherDisponiblesParType(String type) {
        if (type == null || type.trim().isEmpty()) {
            System.err.println("❌ Erreur: Le paramètre 'type' est null ou vide");
            return List.of();
        }
        String typeUpper = type.toUpperCase().trim();
        System.out.println("🔍 Tool: rechercher_disponibles_par_type - type=" + typeUpper);
        List<Location> locations = locationService.getAvailableLocationsByType(typeUpper);
        System.out.println("✅ " + locations.size() + " locations disponibles de type " + typeUpper + " trouvées");
        return locations;
    }

    @Tool(
        name = "details_location",
        description = "Retourne les détails COMPLETS d'une location spécifique par son ID"
    )
    public Location detailsLocation(Long id) {
        System.out.println("🔍 Tool: details_location - id=" + id);
        Location location = locationService.getLocationById(id);
        if (location != null) {
            System.out.println("✅ Location trouvée: " + location.getNom());
        }
        return location;
    }

    @Tool(
        name = "calculer_prix_location",
        description = "Calcule le prix TOTAL d'une location pour un nombre de jours spécifié"
    )
    public Double calculerPrixLocation(Long locationId, Integer nombreJours) {
        System.out.println("🔍 Tool: calculer_prix_location - id=" + locationId + ", jours=" + nombreJours);
        Location location = locationService.getLocationById(locationId);
        if (location != null) {
            Double prixTotal = location.getPrixParJour() * nombreJours;
            System.out.println("✅ Calcul: " + location.getPrixParJour() + "€/jour × " + nombreJours + " jours = " + prixTotal + "€");
            return prixTotal;
        }
        return 0.0;
    }
}