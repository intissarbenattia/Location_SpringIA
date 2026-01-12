package com.smartrent.auth_service.repository;


import com.smartrent.auth_service.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, String> {
}

