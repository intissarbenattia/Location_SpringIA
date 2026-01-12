package com.smartrent.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartrent.reservation_service.dto.LocationDto;



@FeignClient(name = "location-service")
public interface LocationFeignClient {
    
    @GetMapping("/locations/{id}")
    LocationDto getLocationById(@PathVariable Long id);
}