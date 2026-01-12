package com.smartrent.location_service.repository;

import com.smartrent.location_service.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    List<Location> findByDisponible(Boolean disponible);
    
    List<Location> findByType(String type);
    
    List<Location> findByTypeAndDisponible(String type, Boolean disponible);
}