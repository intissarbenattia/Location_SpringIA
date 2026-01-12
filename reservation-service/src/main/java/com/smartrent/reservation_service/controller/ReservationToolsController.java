package com.smartrent.reservation_service.controller;

import com.smartrent.reservation_service.entities.Reservation;
import com.smartrent.reservation_service.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/mcp")
@CrossOrigin("*")
public class ReservationToolsController {

    private final ReservationService service;

    public ReservationToolsController(ReservationService service) {
        this.service = service;
    }

    /**
     * 📋 Liste tous les tools disponibles
     * GET /api/tools
     */
    @GetMapping("tools")
    public Map<String, Object> listTools() {
        return Map.of(
                "service", "reservation-service",
                "version", "1.0.0",
                "tools", List.of(
                        "rechercher_reservations_confirmees",
                        "rechercher_reservations_client",
                        "details_reservation",
                        "reservations_par_location",
                        "rechercher_reservations",
                        "rechercher_reservations_par_statut"
                ),
                "usage", Map.of(
                        "endpoint", "/api/tools/execute",
                        "method", "POST",
                        "example", Map.of(
                                "tool", "rechercher_reservations_client",
                                "params", Map.of("email", "john@example.com")
                        )
                )
        );
    }

    /**
     * ⚡ Exécute n'importe quel tool
     * POST /api/tools/execute
     * Body: {"tool": "nom_du_tool", "params": {...}}
     */
    @PostMapping("/execute")
    public Map<String, Object> executeTool(@RequestBody Map<String, Object> request) {
        String tool = (String) request.get("tool");
        Map<String, Object> params = (Map<String, Object>) request.getOrDefault("params", Map.of());

        try {
            Object result = executeToolLogic(tool, params);
            return Map.of(
                    "success", true,
                    "tool", tool,
                    "result", result
            );
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "tool", tool,
                    "error", e.getMessage()
            );
        }
    }

    /**
     * 🔍 Exécute le tool avec validation
     */
    private Object executeToolLogic(String tool, Map<String, Object> params) {
        return switch (tool) {
            case "rechercher_reservations_confirmees" ->
                    service.getReservationsByStatut("CONFIRMEE");

            case "rechercher_reservations_client" -> {
                String email = getRequiredParam(params, "email");
                yield service.getReservationsByClient(email);
            }

            case "details_reservation" -> {
                Long id = getRequiredLongParam(params, "reservationId");
                yield service.getReservationById(id);
            }

            case "reservations_par_location" -> {
                Long locationId = getRequiredLongParam(params, "locationId");
                yield service.getReservationsByLocation(locationId);
            }

            case "rechercher_reservations" ->
                    service.getAllReservations();

            case "rechercher_reservations_par_statut" -> {
                String statut = getRequiredParam(params, "statut");
                yield service.getReservationsByStatut(statut);
            }

            default -> throw new IllegalArgumentException(
                    "Tool inconnu: " + tool + ". Tools disponibles: " +
                            "rechercher_reservations_confirmees, rechercher_reservations_client, " +
                            "details_reservation, reservations_par_location, rechercher_reservations, " +
                            "rechercher_reservations_par_statut"
            );
        };
    }

    // 🛠️ Helpers pour validation
    private String getRequiredParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null || value.toString().isEmpty()) {
            throw new IllegalArgumentException("Le paramètre '" + key + "' est requis");
        }
        return value.toString();
    }

    private Long getRequiredLongParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Le paramètre '" + key + "' est requis");
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le paramètre '" + key + "' doit être un nombre");
        }
    }
}