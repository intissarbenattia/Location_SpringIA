package com.smartrent.reservation_service;

import com.smartrent.reservation_service.entities.Reservation;
import com.smartrent.reservation_service.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = "com.smartrent.reservation_service")
@EnableDiscoveryClient
@EnableFeignClients
public class ReservationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    CommandLineRunner initReservations(ReservationRepository reservationRepository) {
        return args -> {
            // Toujours vider et réinitialiser pour garantir les bonnes données
            reservationRepository.deleteAll();
            
            // RÉSERVATIONS CONFIRMÉES
                reservationRepository.save(
                    Reservation.builder()
                        .locationId(1L)
                        .clientNom("Ahmed Ben Ali")
                        .clientEmail("ahmed@example.com")
                        .dateDebut(LocalDate.now().plusDays(7))
                        .dateFin(LocalDate.now().plusDays(10))
                        .statut("CONFIRMÉE")
                        .prixTotal(240.0)
                        .build()
                );
                
                reservationRepository.save(
                    Reservation.builder()
                        .locationId(3L)
                        .clientNom("Mohamed Karim")
                        .clientEmail("mohamed@example.com")
                        .dateDebut(LocalDate.now().plusDays(3))
                        .dateFin(LocalDate.now().plusDays(8))
                        .statut("CONFIRMÉE")
                        .prixTotal(225.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(2L)
                        .clientNom("Claire Moreau")
                        .clientEmail("claire.moreau@email.com")
                        .dateDebut(LocalDate.now().plusDays(5))
                        .dateFin(LocalDate.now().plusDays(9))
                        .statut("CONFIRMÉE")
                        .prixTotal(240.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(4L)
                        .clientNom("Luc Fontaine")
                        .clientEmail("luc.fontaine@email.com")
                        .dateDebut(LocalDate.now().plusDays(10))
                        .dateFin(LocalDate.now().plusDays(15))
                        .statut("CONFIRMÉE")
                        .prixTotal(200.0)
                        .build()
                );

                // RÉSERVATIONS EN ATTENTE
                reservationRepository.save(
                    Reservation.builder()
                        .locationId(2L)
                        .clientNom("Fatma Trabelsi")
                        .clientEmail("fatma@example.com")
                        .dateDebut(LocalDate.now().plusDays(14))
                        .dateFin(LocalDate.now().plusDays(21))
                        .statut("EN_ATTENTE")
                        .prixTotal(1750.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(5L)
                        .clientNom("Pierre Leclerc")
                        .clientEmail("pierre.leclerc@email.com")
                        .dateDebut(LocalDate.now().plusDays(15))
                        .dateFin(LocalDate.now().plusDays(22))
                        .statut("EN_ATTENTE")
                        .prixTotal(840.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(6L)
                        .clientNom("Antoine Renard")
                        .clientEmail("antoine.renard@email.com")
                        .dateDebut(LocalDate.now().plusDays(20))
                        .dateFin(LocalDate.now().plusDays(27))
                        .statut("EN_ATTENTE")
                        .prixTotal(1500.0)
                        .build()
                );

                // RÉSERVATIONS ANNULÉES
                reservationRepository.save(
                    Reservation.builder()
                        .locationId(7L)
                        .clientNom("Sophie Bernard")
                        .clientEmail("sophie.bernard@email.com")
                        .dateDebut(LocalDate.now().minusDays(30))
                        .dateFin(LocalDate.now().minusDays(25))
                        .statut("ANNULÉE")
                        .prixTotal(750.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(8L)
                        .clientNom("Nathalie Petit")
                        .clientEmail("nathalie.petit@email.com")
                        .dateDebut(LocalDate.now().plusDays(25))
                        .dateFin(LocalDate.now().plusDays(28))
                        .statut("EN_ATTENTE")
                        .prixTotal(360.0)
                        .build()
                );

                reservationRepository.save(
                    Reservation.builder()
                        .locationId(9L)
                        .clientNom("Jean Martin")
                        .clientEmail("jean.martin@email.com")
                        .dateDebut(LocalDate.now().plusDays(10))
                        .dateFin(LocalDate.now().plusDays(15))
                        .statut("CONFIRMÉE")
                        .prixTotal(250.0)
                        .build()
                );

                System.out.println("✅ " + reservationRepository.count() + " Réservations initialisées avec Builder Pattern");
        };
    }
}