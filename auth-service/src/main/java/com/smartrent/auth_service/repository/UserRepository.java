// ========== UserRepository.java ==========
package com.smartrent.auth_service.repository;

import com.smartrent.auth_service.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    // Méthode pour trouver un utilisateur par son nom
    AppUser findByUsername(String username);
}







