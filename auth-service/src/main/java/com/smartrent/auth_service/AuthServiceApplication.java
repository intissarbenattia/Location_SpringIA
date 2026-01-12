package com.smartrent.auth_service;

import com.smartrent.auth_service.entities.AppRole;
import com.smartrent.auth_service.entities.AppUser;
import com.smartrent.auth_service.repository.RoleRepository;
import com.smartrent.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Classe principale de l'application Auth Service.
 * Responsable du démarrage de l'application et de l'initialisation des données.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    /**
     * Initialisation des données au démarrage.
     */
    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            System.out.println("\n========================================");
            System.out.println("🚀 INITIALISATION DES DONNÉES");
            System.out.println("========================================\n");

            /* ======================
               Création des rôles
               ====================== */
            if (roleRepository.count() == 0) {

                AppRole userRole = new AppRole();
                userRole.setNom("USER");
                roleRepository.save(userRole);

                AppRole adminRole = new AppRole();
                adminRole.setNom("ADMIN");
                roleRepository.save(adminRole);

                System.out.println("✅ Rôles créés : USER, ADMIN");
            }

            AppRole roleUser = roleRepository.findById("USER").get();
            AppRole roleAdmin = roleRepository.findById("ADMIN").get();

            /* ======================
               Création des utilisateurs
               ====================== */
            if (userRepository.count() == 0) {

                // Utilisateur USER
                AppUser user = new AppUser();
                user.setId(UUID.randomUUID().toString());
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@smartrent.com");
                user.setRoles(List.of(roleUser));

                userRepository.save(user);
                System.out.println("✅ Utilisateur créé : user / user123 (USER)");

                // Administrateur USER + ADMIN
                AppUser admin = new AppUser();
                admin.setId(UUID.randomUUID().toString());
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@smartrent.com");
                admin.setRoles(Arrays.asList(roleUser, roleAdmin));

                userRepository.save(admin);
                System.out.println("✅ Administrateur créé : admin / admin123 (USER, ADMIN)");

                System.out.println("\n========================================");
                System.out.println("✅ Initialisation terminée avec succès !");
                System.out.println("========================================\n");

                System.out.println("📋 COMPTES DE TEST :");
                System.out.println("   • user  / user123");
                System.out.println("   • admin / admin123\n");

            } else {
                System.out.println("ℹ️  Les utilisateurs existent déjà dans la base de données.\n");
            }
        };
    }
}
