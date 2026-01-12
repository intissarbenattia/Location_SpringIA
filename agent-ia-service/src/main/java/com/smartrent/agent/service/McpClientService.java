package com.smartrent.agent.service;

import com.smartrent.agent.config.McpClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class McpClientService {

    private static final Logger log = LoggerFactory.getLogger(McpClientService.class);

    private final McpClientConfig mcpConfig;
    private final WebClient webClient;

    public McpClientService(McpClientConfig mcpConfig, WebClient webClient) {
        this.mcpConfig = mcpConfig;
        this.webClient = webClient;
    }

    /**
     * Liste tous les outils disponibles d'un service MCP
     */
    public Mono<Map<String, Object>> listTools(String serviceName) {
        McpClientConfig.ServiceConfig config = mcpConfig.getServices().get(serviceName);
        if (config == null) {
            return Mono.error(new RuntimeException("Service MCP inconnu: " + serviceName));
        }

        String url = config.getUrl() + config.getEndpoint() + "/tools";
        log.info("🔍 Listing tools from: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnSuccess(result -> log.info("✅ Tools retrieved from {}: {}", serviceName, result))
                .doOnError(error -> log.error("❌ Error listing tools from {}: {}", serviceName, error.getMessage()));
    }

    /**
     * Exécute un outil MCP
     */
    public Mono<Map<String, Object>> executeTool(String serviceName, String toolName, Map<String, Object> parameters) {
        McpClientConfig.ServiceConfig config = mcpConfig.getServices().get(serviceName);
        if (config == null) {
            return Mono.error(new RuntimeException("Service MCP inconnu: " + serviceName));
        }

        String url = config.getUrl() + config.getEndpoint() + "/execute";

        Map<String, Object> request = new HashMap<>();
        request.put("tool", toolName);
        request.put("params", parameters != null ? parameters : new HashMap<>());  // ✅ CHANGÉ ICI

        log.info("🚀 Executing tool: {} on service: {} with params: {}", toolName, serviceName, parameters);

        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnSuccess(result -> log.info("✅ Tool executed: {}", result))
                .doOnError(error -> log.error("❌ Error executing tool: {}", error.getMessage()));
    }

    /**
     * Récupère tous les outils disponibles de tous les services
     */
    public Mono<Map<String, Object>> getAllAvailableTools() {
        if (mcpConfig.getServices() == null || mcpConfig.getServices().isEmpty()) {
            log.warn("⚠️ Aucun service MCP configuré");
            return Mono.just(new HashMap<>());
        }

        List<Mono<Map.Entry<String, Object>>> toolMonos = new ArrayList<>();

        for (String serviceName : mcpConfig.getServices().keySet()) {
            Mono<Map.Entry<String, Object>> mono = listTools(serviceName)
                    .map(tools -> Map.entry(serviceName, (Object) tools))
                    .onErrorResume(e -> {
                        log.warn("⚠️ Service {} non disponible: {}", serviceName, e.getMessage());
                        return Mono.empty();
                    });
            toolMonos.add(mono);
        }

        return Mono.zip(toolMonos, objects -> {
            Map<String, Object> allTools = new HashMap<>();
            for (Object obj : objects) {
                if (obj instanceof Map.Entry) {
                    @SuppressWarnings("unchecked")
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
                    allTools.put(entry.getKey(), entry.getValue());
                }
            }
            return allTools;
        });
    }
}