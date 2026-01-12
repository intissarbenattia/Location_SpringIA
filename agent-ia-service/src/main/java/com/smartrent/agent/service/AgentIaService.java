package com.smartrent.agent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;

@Service
public class AgentIaService {

    private static final Logger log = LoggerFactory.getLogger(AgentIaService.class);

    private final OllamaChatModel chatModel;
    private final McpClientService mcpClientService;

    public AgentIaService(OllamaChatModel chatModel, McpClientService mcpClientService) {
        this.chatModel = chatModel;
        this.mcpClientService = mcpClientService;
    }
    private final Map<String, List<Message>> conversationHistory = new HashMap<>();

    private final Map<String, String> toolService = Map.ofEntries(
        Map.entry("rechercher_locations", "location"),
        Map.entry("rechercher_locations_disponibles", "location"),
        Map.entry("rechercher_par_type", "location"),
        Map.entry("rechercher_disponibles_par_type", "location"),
        Map.entry("rechercher_appartements_disponibles", "location"),
        Map.entry("rechercher_maisons_disponibles", "location"),
        Map.entry("rechercher_studios_disponibles", "location"),
        Map.entry("rechercher_voitures_disponibles", "location"),
        Map.entry("details_location", "location"),
        Map.entry("calculer_prix_location", "location"),
        Map.entry("rechercher_reservations", "reservation"),
        Map.entry("rechercher_reservations_confirmees", "reservation"),
        Map.entry("rechercher_reservations_client", "reservation"),
        Map.entry("rechercher_reservations_par_statut", "reservation"),
        Map.entry("details_reservation", "reservation"),
        Map.entry("reservations_par_location", "reservation")
    );

    private final Map<String, List<String>> toolParams = Map.ofEntries(
        Map.entry("rechercher_locations", List.of()),
        Map.entry("rechercher_locations_disponibles", List.of()),
        Map.entry("rechercher_par_type", List.of("type")),
        Map.entry("rechercher_disponibles_par_type", List.of("type")),
        Map.entry("rechercher_appartements_disponibles", List.of()),
        Map.entry("rechercher_maisons_disponibles", List.of()),
        Map.entry("rechercher_studios_disponibles", List.of()),
        Map.entry("rechercher_voitures_disponibles", List.of()),
        Map.entry("details_location", List.of("locationId")),
        Map.entry("calculer_prix_location", List.of("locationId", "nombreJours")),
        Map.entry("rechercher_reservations", List.of()),
        Map.entry("rechercher_reservations_confirmees", List.of()),
        Map.entry("rechercher_reservations_client", List.of("email")),
        Map.entry("rechercher_reservations_par_statut", List.of("statut")),
        Map.entry("details_reservation", List.of("reservationId")),
        Map.entry("reservations_par_location", List.of("locationId"))
    );

    private final Map<String, PendingTool> pendingTools = new HashMap<>();

    private static class PendingTool {
        String service;
        String toolName;
        List<String> requiredParams;
        Map<String, Object> params = new HashMap<>();

        public PendingTool(String service, String toolName, List<String> requiredParams) {
            this.service = service;
            this.toolName = toolName;
            this.requiredParams = requiredParams;
        }
    }

    private static final String SYSTEM_PROMPT = """
        RÔLE: Assistant professionnel de location immobilière SmartRent.
        
        RÈGLES STRICTES:
        1. Répondre UNIQUEMENT aux questions sur les locations et réservations
        2. Utiliser EXCLUSIVEMENT les outils MCP pour obtenir les données réelles
        3. Pour toute question hors domaine: répondre "Je ne sais pas"
        4. INTERDICTION ABSOLUE d'inventer ou supposer des informations
        5. Présenter les données sous forme de liste claire et professionnelle
        
        OUTILS DISPONIBLES (16 total):
        
        LOCATION SERVICE (10 outils):
        - rechercher_locations : Toutes les locations
        - rechercher_locations_disponibles : Locations disponibles uniquement
        - rechercher_par_type(type) : Locations par type
        - rechercher_appartements_disponibles : Appartements disponibles
        - rechercher_maisons_disponibles : Maisons disponibles
        - rechercher_studios_disponibles : Studios disponibles
        - rechercher_voitures_disponibles : Voitures disponibles
        - rechercher_disponibles_par_type(type) : Disponibles par type
        - details_location(id) : Détails complets d'une location
        - calculer_prix_location(id, jours) : Calcul du prix total
        
        RESERVATION SERVICE (6 outils):
        - rechercher_reservations : Toutes les réservations
        - rechercher_reservations_confirmees : Réservations confirmées
        - rechercher_reservations_client(email) : Réservations d'un client
        - rechercher_reservations_par_statut(statut) : Par statut (EN_ATTENTE, CONFIRMÉE, ANNULÉE)
        - details_reservation(id) : Détails d'une réservation
        - reservations_par_location(locationId) : Réservations d'une location
        
        FORMAT DE RÉPONSE OBLIGATOIRE:
        
        1. UNE LIGNE INTRODUCTIVE professionnelle (1 phrase courte)
        2. LISTE STRUCTURÉE avec tirets (-) pour chaque élément
        3. Format par ligne:
           - [TYPE] Nom: caractéristiques principales | Prix | Statut
        4. UNE LIGNE DE CONCLUSION (optionnelle, si pertinent)
        
        EXEMPLE DE FORMAT ATTENDU:
        
        Voici les locations disponibles dans notre catalogue:
        
        - [APPARTEMENT] Studio Centre-Ville Tunis: 35m², vue mer, moderne | 80€/jour | Disponible
        - [MAISON] Villa Plage Hammamet: 4 chambres, piscine, accès direct plage | 250€/jour | Disponible
        - [STUDIO] Loft Design Paris Bastille: 45m², style industriel, équipé | 120€/jour | Disponible
        - [VOITURE] Peugeot 208: économique, 5 portes, climatisation | 35€/jour | Disponible
        
        Total: 4 locations disponibles.
        
        STYLE PROFESSIONNEL:
        - Ton formel et courtois
        - Informations factuelles et précises
        - Pas d'emojis dans la réponse finale
        - Concision et clarté
        - Données chiffrées exactes des outils MCP
        
        INTERDICTIONS:
        - Ne JAMAIS utiliser de paragraphes longs
        - Ne JAMAIS inventer d'informations
        - Ne JAMAIS répondre sans consulter les outils
        - Ne JAMAIS mélanger les formats (rester en liste)
        - Ne JAMAIS traiter de sujets hors locations/réservations
        
        ACTIONS OBLIGATOIRES:
        - Toujours appeler les outils MCP appropriés
        - Extraire et présenter les données réelles
        - Formater en liste claire avec tirets
        - Citer les chiffres exacts
        - Maintenir un ton professionnel constant
        """;

    /**
     * Génère une réponse de l'agent IA
     */
    public Mono<String> chat(String sessionId, String userMessage) {
        log.info("💬 User message [{}]: {}", sessionId, userMessage);

        // Initialiser l'historique si nécessaire
        conversationHistory.putIfAbsent(sessionId, new ArrayList<>());

        // Ajouter le message utilisateur
        conversationHistory.get(sessionId).add(new UserMessage(userMessage));

        // Analyser et exécuter les outils nécessaires
        return analyzeAndExecute(userMessage)
                .flatMap(toolResult -> {
                    // Construire le prompt avec les résultats
                    String enhancedPrompt = buildEnhancedPrompt(userMessage, toolResult);

                    // Générer la réponse
                    return generateResponse(sessionId, enhancedPrompt);
                })
                .onErrorResume(e -> {
                    log.error("❌ Error in chat: {}", e.getMessage(), e);
                    return Mono.just("Désolé, une erreur s'est produite: " + e.getMessage());
                });
    }

    /**
     * Analyse le message et exécute les outils nécessaires
     */
    private Mono<String> analyzeAndExecute(String userMessage) {
        String messageLower = userMessage.toLowerCase();
        
        log.info("🔍 Analyse du message: {}", userMessage);

        // VALIDATION STRICTE: Vérifier si la question est pertinente
        if (!isProductRelated(messageLower)) {
            log.warn("⚠️ Question hors domaine détectée: {}", userMessage);
            return Mono.just("");
        }

        // LOCATIONS
        if (messageLower.contains("location") || messageLower.contains("bien") ||
                messageLower.contains("appartement") || messageLower.contains("maison") ||
                messageLower.contains("studio") || messageLower.contains("voiture")) {

            if (messageLower.contains("appartement") && messageLower.contains("disponible")) {
                return executeLocationTool("rechercher_appartements_disponibles", Map.of());
            } else if (messageLower.contains("maison") && messageLower.contains("disponible")) {
                return executeLocationTool("rechercher_maisons_disponibles", Map.of());
            } else if (messageLower.contains("studio") && messageLower.contains("disponible")) {
                return executeLocationTool("rechercher_studios_disponibles", Map.of());
            } else if (messageLower.contains("voiture") && messageLower.contains("disponible")) {
                return executeLocationTool("rechercher_voitures_disponibles", Map.of());
            } else if (messageLower.contains("appartement")) {
                return executeLocationTool("rechercher_par_type", Map.of("type", "APPARTEMENT"));
            } else if (messageLower.contains("maison")) {
                return executeLocationTool("rechercher_par_type", Map.of("type", "MAISON"));
            } else if (messageLower.contains("studio")) {
                return executeLocationTool("rechercher_par_type", Map.of("type", "STUDIO"));
            } else if (messageLower.contains("voiture")) {
                return executeLocationTool("rechercher_par_type", Map.of("type", "VOITURE"));
            } else if (messageLower.contains("disponible")) {
                return executeLocationTool("rechercher_locations_disponibles", Map.of());
            } else {
                return executeLocationTool("rechercher_locations", Map.of());
            }
        }

        // RÉSERVATIONS
        if (messageLower.contains("reservation") || messageLower.contains("réservation") ||
                messageLower.contains("réserver") || messageLower.contains("reserver")) {

            if (messageLower.contains("confirmée") || messageLower.contains("confirmee")) {
                return executeReservationTool("rechercher_reservations_confirmees", Map.of());
            } else if (messageLower.contains("client") && messageLower.contains("@")) {
                String email = extractEmail(userMessage);
                if (email != null) {
                    return executeReservationTool("rechercher_reservations_client", Map.of("email", email));
                }
            } else {
                return executeReservationTool("rechercher_reservations", Map.of());
            }
        }

        // PRIX
        if (messageLower.contains("prix") || messageLower.contains("coût") || messageLower.contains("cout")) {
            return Mono.just("Pour calculer le prix, j'ai besoin de l'ID de la location et du nombre de jours.");
        }

        return Mono.just("");
    }

    /**
     * Exécute un outil du service Location
     */
    private Mono<String> executeLocationTool(String toolName, Map<String, Object> params) {
        return mcpClientService.executeTool("location", toolName, params)
                .map(result -> formatToolResult("Location", toolName, result))
                .onErrorReturn("Erreur lors de l'appel au service Location");
    }

    /**
     * Exécute un outil du service Reservation
     */
    private Mono<String> executeReservationTool(String toolName, Map<String, Object> params) {
        return mcpClientService.executeTool("reservation", toolName, params)
                .map(result -> formatToolResult("Reservation", toolName, result))
                .onErrorReturn("Erreur lors de l'appel au service Reservation");
    }

    /**
     * Formate le résultat d'un outil
     */
    private String formatToolResult(String service, String tool, Map<String, Object> result) {
        if (Boolean.TRUE.equals(result.get("success"))) {
            Object data = result.get("result");
            return String.format("[Service %s - Outil %s]\nRésultat: %s", service, tool, data);
        } else {
            return String.format("[Service %s - Outil %s]\nErreur: %s", service, tool, result.get("error"));
        }
    }

    /**
     * Construit un prompt enrichi avec les résultats des outils
     */
    private String buildEnhancedPrompt(String userMessage, String toolResult) {
        if (toolResult.isEmpty()) {
            if (!isProductRelated(userMessage)) {
                return "L'utilisateur demande: \"" + userMessage + "\"\n\nRéponds EXACTEMENT: \"Je ne sais pas\"";
            }
            return userMessage;
        }

        return String.format("""
            Question de l'utilisateur: %s
            
            Données obtenues des services MCP:
            %s
            
            Réponds selon les instructions du SYSTEM_PROMPT (format liste avec tirets).
            """, userMessage, toolResult);
    }
    
    /**
     * Vérifie si la question concerne les locations/réservations
     */
    private boolean isProductRelated(String message) {
        String lower = message.toLowerCase();
        return lower.contains("location") || lower.contains("appartement") || 
               lower.contains("maison") || lower.contains("studio") || 
               lower.contains("voiture") || lower.contains("réservation") ||
               lower.contains("reservation") || lower.contains("disponible") ||
               lower.contains("prix") || lower.contains("coût") || lower.contains("cout") ||
               lower.contains("détail") || lower.contains("detail") || lower.contains("statut");
    }

    /**
     * Génère la réponse de l'IA
     */
    private Mono<String> generateResponse(String sessionId, String prompt) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM_PROMPT));
        messages.addAll(conversationHistory.get(sessionId));
        messages.add(new UserMessage(prompt));

        try {
            ChatResponse response = chatModel.call(new Prompt(messages));
            Generation generation = response.getResult();
            AssistantMessage assistantMessage = generation.getOutput();
            String aiResponse = extractMessageText(assistantMessage);

            conversationHistory.get(sessionId).add(new AssistantMessage(aiResponse));

            log.info("🤖 AI response [{}]: {}", sessionId, aiResponse);
            return Mono.just(aiResponse);

        } catch (Exception e) {
            log.error("❌ Error generating response: {}", e.getMessage(), e);
            return Mono.error(e);
        }
    }

    /**
     * Méthode robuste pour extraire le texte d'un AssistantMessage
     */
    private String extractMessageText(AssistantMessage message) {
        try {
            return (String) message.getClass().getMethod("getContent").invoke(message);
        } catch (Exception e1) {
            try {
                return (String) message.getClass().getMethod("getText").invoke(message);
            } catch (Exception e2) {
                try {
                    return (String) message.getClass().getMethod("getMessageContent").invoke(message);
                } catch (Exception e3) {
                    log.warn("⚠️ Impossible de trouver la méthode appropriée, utilisation de toString()");
                    String text = message.toString();
                    if (text.startsWith("AssistantMessage[")) {
                        text = text.substring(text.indexOf("[") + 1, text.lastIndexOf("]"));
                    }
                    return text;
                }
            }
        }
    }

    /**
     * Extrait un email d'un message
     */
    private String extractEmail(String message) {
        String[] words = message.split("\\s+");
        for (String word : words) {
            if (word.contains("@") && word.contains(".")) {
                return word;
            }
        }
        return null;
    }

    /**
     * Réinitialise l'historique de conversation
     */
    public void clearHistory(String sessionId) {
        conversationHistory.remove(sessionId);
        log.info("🗑️ History cleared for session: {}", sessionId);
    }
}