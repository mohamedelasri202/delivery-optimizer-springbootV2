package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.DTO.AiTrainingDTO;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Repositories.DeliveryHistoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("ai")
@Qualifier("tourOptimizer")
public class AIOptimizer implements TourOptimizer {

    private final ChatClient aiClient;
    private final DeliveryHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    public AIOptimizer(ChatClient aiClient, DeliveryHistoryRepository historyRepository, ObjectMapper objectMapper) {
        this.aiClient = aiClient;
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Delivery> calculateOptimalTour(RouteOptimizationContext context) {
        LocalDate startDate = LocalDate.now().minusDays(90);
        List<AiTrainingDTO> historyData = historyRepository.findAiTrainingData(startDate);

        if (historyData.isEmpty()) {
            log.warn("AI optimization failed: No historical training data found since {}. Returning original order.", startDate);

            return context.getDeliveries();
        }

        String jsonHistory = formatHistoryToJson(historyData);
        String prompt = constructPrompt(jsonHistory, context.getDeliveries());

        try {
            String rawJsonResponse = aiClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.debug("RAW AI RESPONSE: {}", rawJsonResponse);


            return parseOptimizedRoute(rawJsonResponse, context.getDeliveries());

        } catch (Exception e) {
            log.error("AI optimization failed with error: {}. Returning original order.", e.getMessage());

            return context.getDeliveries();
        }
    }

    private String formatHistoryToJson(List<AiTrainingDTO> historyData) {
        try {
            return objectMapper.writeValueAsString(historyData);
        } catch (Exception e) {
            log.error("Failed to convert historical data to JSON for AI prompt.", e);
            throw new RuntimeException("Data formatting error for AI.", e);
        }
    }
    private String constructPrompt(String jsonHistory, List<Delivery> currentDeliveries) {

        String deliveriesJson = currentDeliveries.stream()
                .map(d -> String.format("{\"id\": %d, \"lat\": %.6f, \"lon\": %.6f, \"weight\": %.2f}",
                        d.getId(), d.getLatitude(), d.getLongitude(), d.getWeight()))
                .collect(Collectors.joining(", ", "[", "]"));


        String allIds = currentDeliveries.stream()
                .map(d -> String.valueOf(d.getId()))
                .collect(Collectors.joining(", "));

        return String.format(
                "You are a delivery route optimizer.\n\n" +

                        "Historical delivery data:\n%s\n\n" +

                        "Current deliveries to optimize:\n%s\n\n" +

                        "IMPORTANT:\n" +
                        "- You have exactly %d deliveries with IDs: %s\n" +
                        "- You MUST include ALL %d delivery IDs in your response\n" +
                        "- Return them in the optimal order based on distance, weight, and historical patterns\n" +
                        "- Each ID must appear exactly once\n\n" +

                        "Return ONLY this JSON format:\n" +
                        "i dont want no text nothing from u dont say hey dont say a word return only the json format data that's it just response with json no text afte or before not commentary at all from u not a single word "+
                        "{\"ordered_delivery_ids\": [%s], \"reason\": \"brief explanation\"}\n\n" +

                        "Optimize and return JSON:",
                jsonHistory,
                deliveriesJson,
                currentDeliveries.size(),
                allIds,
                currentDeliveries.size(),
                allIds
        );
    }

    private List<Delivery> parseOptimizedRoute(String rawJsonResponse, List<Delivery> originalDeliveries) {
        try {
            log.info("========== AI RESPONSE DEBUG ==========");
            log.info("RAW AI RESPONSE: {}", rawJsonResponse);

            // Step 1: Clean the response
            String cleaned = rawJsonResponse
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .replaceAll(";\\s*$", "")  // Remove trailing semicolon
                    .replaceAll("//.*", "")     // Remove comments
                    .trim();

            log.info("After cleaning: {}", cleaned);


            int firstBrace = cleaned.indexOf('{');
            int lastBrace = cleaned.lastIndexOf('}');

            if (firstBrace == -1 || lastBrace == -1 || firstBrace >= lastBrace) {
                log.warn("No valid JSON object found. Returning original order.");
                return originalDeliveries;
            }

            String jsonBlock = cleaned.substring(firstBrace, lastBrace + 1);
            log.info("EXTRACTED JSON: {}", jsonBlock);


            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(jsonBlock);
            } catch (Exception e) {

                log.info("First parse attempt failed, trying to fix JSON...");
                String fixed = fixInvalidJson(jsonBlock);
                log.info("Fixed JSON: {}", fixed);
                rootNode = objectMapper.readTree(fixed);
            }

            log.info("Parsed JSON successfully");


            JsonNode orderedIdsNode = null;
            String[] possibleFieldNames = {
                    "ordered_delivery_ids",
                    "orderedDeliveryIds",
                    "resultingOrderedDeliveriesIDs",
                    "delivery_ids",
                    "order",
                    "ids"
            };

            for (String fieldName : possibleFieldNames) {
                orderedIdsNode = rootNode.get(fieldName);
                if (orderedIdsNode != null) {
                    log.info("Found delivery IDs under field: '{}'", fieldName);
                    break;
                }
            }

            if (orderedIdsNode == null || !orderedIdsNode.isArray()) {
                log.warn("Could not find delivery IDs array. Available fields: {}", rootNode.toPrettyString());
                return originalDeliveries;
            }


            Map<Long, Delivery> deliveryMap = originalDeliveries.stream()
                    .collect(Collectors.toMap(Delivery::getId, d -> d));

            List<Delivery> orderedDeliveries = new ArrayList<>();

            for (JsonNode node : orderedIdsNode) {
                long deliveryId;

                // Handle if it's a plain number
                if (node.isNumber()) {
                    deliveryId = node.asLong();
                }
                // Handle if it's an object with "id" field
                else if (node.isObject() && node.has("id")) {
                    deliveryId = node.get("id").asLong();
                }

                else if (node.isObject()) {
                    JsonNode idNode = node.get("delivery_id");
                    if (idNode == null) idNode = node.get("deliveryId");
                    if (idNode != null) {
                        deliveryId = idNode.asLong();
                    } else {
                        log.warn("Could not extract ID from object: {}", node);
                        continue;
                    }
                } else {
                    log.warn("Unexpected node type: {}", node);
                    continue;
                }

                Delivery delivery = deliveryMap.get(deliveryId);
                if (delivery != null) {
                    orderedDeliveries.add(delivery);
                    log.debug("Added delivery ID: {}", deliveryId);
                } else {
                    log.warn("Delivery ID {} not found in current deliveries", deliveryId);
                }
            }

            log.info("AI returned {} out of {} required deliveries",
                    orderedDeliveries.size(), originalDeliveries.size());


            if (orderedDeliveries.size() != originalDeliveries.size()) {
                log.warn("AI returned {} deliveries but expected {}. Returning original order.",
                        orderedDeliveries.size(), originalDeliveries.size());
                return originalDeliveries;
            }

            String reasonField = rootNode.has("reason") ? "reason" : "explanation";
            if (rootNode.has(reasonField)) {
                log.info("AI Optimization reason: {}", rootNode.get(reasonField).asText());
            }

            log.info(" Successfully optimized route. Order: {}",
                    orderedDeliveries.stream().map(Delivery::getId).collect(Collectors.toList()));

            return orderedDeliveries;

        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", e.getMessage());
            log.debug("Stack trace:", e);
            return originalDeliveries;
        }
    }


    private String fixInvalidJson(String json) {

        String fixed = json.replaceAll("(\\{|,)\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*:", "$1\"$2\":");

        fixed = fixed.replaceAll(":\\s*([a-zA-Z][a-zA-Z0-9\\s]*)(\\s*[,}])", ":\"$1\"$2");

        return fixed;
    }}