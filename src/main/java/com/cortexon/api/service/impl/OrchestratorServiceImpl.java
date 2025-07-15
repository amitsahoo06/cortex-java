package com.cortexon.api.service.impl;

import com.cortexon.api.model.AgentResponse;
import com.cortexon.api.model.StreamResponse;
import com.cortexon.api.service.OrchestratorService;
import com.cortexon.api.service.PlannerService;
import com.cortexon.api.util.WebSocketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the OrchestratorService interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {

    private final PlannerService plannerService;
    private final ObjectMapper objectMapper;
    private final Map<String, List<StreamResponse>> sessionResponses = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<AgentResponse> processTask(String task) {
        String sessionId = "http-" + System.currentTimeMillis();
        return processTaskInternal(task, sessionId, null);
    }

    @Override
    public CompletableFuture<AgentResponse> processTaskWithWebSocket(String task, WebSocketSession session) {
        return processTaskInternal(task, session.getId(), session);
    }

    @Override
    public List<StreamResponse> getResponses(String sessionId) {
        return sessionResponses.getOrDefault(sessionId, new ArrayList<>());
    }

    private CompletableFuture<AgentResponse> processTaskInternal(String task, String sessionId, WebSocketSession session) {
        // Initialize response collection for this session
        List<StreamResponse> responses = new ArrayList<>();
        sessionResponses.put(sessionId, responses);

        // Create initial orchestrator response
        StreamResponse orchestratorResponse = StreamResponse.builder()
                .agentName("Orchestrator")
                .instructions(task)
                .statusCode(0)
                .build()
                .addStep("Initializing orchestrator...");

        responses.add(orchestratorResponse);

        // Send initial response through WebSocket if available
        if (session != null) {
            try {
                WebSocketUtil.sendMessage(session, objectMapper.writeValueAsString(orchestratorResponse));
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                // Update orchestrator response
                orchestratorResponse.addStep("Agents initialized successfully");
                sendWebSocketUpdate(session, orchestratorResponse);

                // Plan the task
                StreamResponse plannerResponse = StreamResponse.builder()
                        .agentName("Planner Agent")
                        .instructions(task)
                        .statusCode(0)
                        .build()
                        .addStep("Planning task...");

                responses.add(plannerResponse);
                sendWebSocketUpdate(session, plannerResponse);

                // Get plan from planner service
                String plan = plannerService.createPlan(task);
                
                // Update planner response
                plannerResponse.setOutput(plan);
                plannerResponse.setStatusCode(200);
                plannerResponse.addStep("Task planned successfully");
                sendWebSocketUpdate(session, plannerResponse);

                // Update orchestrator response
                orchestratorResponse.addStep("Task planned successfully");
                orchestratorResponse.setOutput("Task completed successfully");
                orchestratorResponse.setStatusCode(200);
                sendWebSocketUpdate(session, orchestratorResponse);

                // Return final response
                return AgentResponse.builder()
                        .data("Task completed successfully")
                        .responses(responses)
                        .build();
            } catch (Exception e) {
                log.error("Error processing task", e);
                
                // Update orchestrator response with error
                orchestratorResponse.setOutput("Error: " + e.getMessage());
                orchestratorResponse.setStatusCode(500);
                sendWebSocketUpdate(session, orchestratorResponse);
                
                return AgentResponse.builder()
                        .data("Error: " + e.getMessage())
                        .responses(responses)
                        .build();
            }
        });
    }

    private void sendWebSocketUpdate(WebSocketSession session, StreamResponse response) {
        if (session != null && session.isOpen()) {
            try {
                String message = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("Error sending WebSocket update", e);
            }
        }
    }
}