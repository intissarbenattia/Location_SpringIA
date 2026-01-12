package com.smartrent.location_service.client;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartrent.location_service.dto.ReservationDto;



@FeignClient(
        name = "reservation-service",
        fallback = ReservationFeignClient.ReservationFallback.class
)
public interface ReservationFeignClient {

    @GetMapping("/api/reservations/location/{locationId}")
    List<ReservationDto> getReservationsByLocation(@PathVariable Long locationId);



    @Component
    class ReservationFallback implements ReservationFeignClient {

        @Override
        public List<ReservationDto> getReservationsByLocation(Long locationId) {
            return Collections.emptyList();
        }
    }
}
