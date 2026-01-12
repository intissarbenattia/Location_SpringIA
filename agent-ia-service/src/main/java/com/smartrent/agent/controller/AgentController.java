package com.smartrent.agent.controller;

import com.smartrent.agent.service.AgentIaService;
import com.smartrent.agent.service.McpClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/agent")
@Slf4j
public class AgentController {

    private static final Logger log = LoggerFactory.getLogger(AgentController.class);

    private final AgentIaService agentIaService;
    private final McpClientService mcpClientService;

    public AgentController(AgentIaService agentIaService, McpClientService mcpClientService) {
        this.agentIaService = agentIaService;
        this.mcpClientService = mcpClientService;
    }

    /**
     * Endpoint de chat
     */
    @PostMapping("/chat")
    public Mono<ResponseEntity<Map<String, Object>>> chat(@RequestBody Map<String, String> request) {
        String sessionId = request.getOrDefault("sessionId", UUID.randomUUID().toString());
        String message = request.get("message");

        if (message == null || message.isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Le message est requis");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        return agentIaService.chat(sessionId, message)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("sessionId", sessionId);
                    result.put("message", message);
                    result.put("response", response);
                    result.put("timestamp", System.currentTimeMillis());
                    return ResponseEntity.ok(result);
                })
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(error));
                });
    }

    /**
     * Liste tous les outils disponibles
     */
    @GetMapping("/tools")
    public Mono<ResponseEntity<Map<String, Object>>> listAllTools() {
        return mcpClientService.getAllAvailableTools()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(error));
                });
    }

    /**
     * Liste les outils d'un service spécifique
     */
    @GetMapping("/tools/{serviceName}")
    public Mono<ResponseEntity<Map<String, Object>>> listServiceTools(@PathVariable String serviceName) {
        return mcpClientService.listTools(serviceName)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Test de santé
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "agent-ia-service");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }

    /**
     * Réinitialise l'historique d'une session
     */
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, String>> clearSession(@PathVariable String sessionId) {
        agentIaService.clearHistory(sessionId);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Session cleared");
        result.put("sessionId", sessionId);
        return ResponseEntity.ok(result);
    }
}