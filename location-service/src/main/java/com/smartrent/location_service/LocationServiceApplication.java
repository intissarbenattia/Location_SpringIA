package com.smartrent.location_service;

import com.smartrent.location_service.entities.Location;
import com.smartrent.location_service.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.smartrent.location_service")

public class LocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initLocations(LocationRepository locationRepository) {
        return args -> {
            // Toujours vider et réinitialiser pour garantir les bonnes données
            locationRepository.deleteAll();
            
            // APPARTEMENTS
                locationRepository.save(
                    Location.builder()
                        .type("APPARTEMENT")
                        .nom("Studio Centre-Ville")
                        .adresse("15 Rue de la République, Tunis")
                        .prixParJour(80.0)
                        .disponible(true)
                        .description("Studio moderne avec vue sur mer")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("APPARTEMENT")
                        .nom("Appartement 2P Marais")
                        .adresse("42 Rue des Francs Bourgeois, 75004 Paris")
                        .prixParJour(80.0)
                        .disponible(true)
                        .description("Bel appartement 2 pièces dans le Marais, proche des transports")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("APPARTEMENT")
                        .nom("Loft moderne Bastille")
                        .adresse("100 Rue de Charonne, 75011 Paris")
                        .prixParJour(120.0)
                        .disponible(true)
                        .description("Loft design avec hauteur sous plafond, lumineux")
                        .build()
                );

                // MAISONS
                locationRepository.save(
                    Location.builder()
                        .type("MAISON")
                        .nom("Villa Seaside")
                        .adresse("45 Avenue Habib Bourguiba, La Marsa")
                        .prixParJour(250.0)
                        .disponible(true)
                        .description("Villa 4 chambres avec piscine")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("MAISON")
                        .nom("Maison Bretagne 4P")
                        .adresse("Rue du Petit Port, 29000 Quimper")
                        .prixParJour(150.0)
                        .disponible(true)
                        .description("Charmante maison de 4 pièces en Bretagne, près de la mer")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("MAISON")
                        .nom("Villa Côte d'Azur")
                        .adresse("Avenue des Pins, 06400 Cannes")
                        .prixParJour(250.0)
                        .disponible(true)
                        .description("Somptueuse villa avec piscine et vue méditerranée")
                        .build()
                );

                // STUDIOS
                locationRepository.save(
                    Location.builder()
                        .type("STUDIO")
                        .nom("Studio Aéroport CDG")
                        .adresse("2 Rue de l'Aviation, 95700 Roissy-en-Brie")
                        .prixParJour(45.0)
                        .disponible(false)
                        .description("Studio pratique près de l'aéroport CDG")
                        .build()
                );

                // VOITURES
                locationRepository.save(
                    Location.builder()
                        .type("VOITURE")
                        .nom("Peugeot 208")
                        .adresse("Agence Centre-Ville")
                        .prixParJour(45.0)
                        .disponible(true)
                        .description("Voiture économique, climatisée")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("VOITURE")
                        .nom("Renault Clio")
                        .adresse("Parking Central, Paris")
                        .prixParJour(40.0)
                        .disponible(true)
                        .description("Voiture de location compacte et économique")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("VOITURE")
                        .nom("BMW Série 5")
                        .adresse("Parking Prestige, Paris")
                        .prixParJour(120.0)
                        .disponible(true)
                        .description("Voiture de prestige pour déplacements professionnels")
                        .build()
                );

                // ÉQUIPEMENTS
                locationRepository.save(
                    Location.builder()
                        .type("EQUIPEMENT")
                        .nom("Vélo électrique")
                        .adresse("Station Vélib', Paris")
                        .prixParJour(15.0)
                        .disponible(true)
                        .description("Vélo électrique pour se déplacer facilement")
                        .build()
                );

                locationRepository.save(
                    Location.builder()
                        .type("EQUIPEMENT")
                        .nom("Trottinette électrique")
                        .adresse("Station Trotty, Paris")
                        .prixParJour(10.0)
                        .disponible(true)
                        .description("Trottinette pratique et ludique")
                        .build()
                );

                System.out.println("✅ " + locationRepository.count() + " Locations initialisées avec Lombok Builder");
        };
    }
}