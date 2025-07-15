package com.cortexon.api.service;

import com.cortexon.api.model.AgentResponse;
import com.cortexon.api.model.StreamResponse;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for orchestrating agent interactions
 */
public interface OrchestratorService {

    /**
     * Processes a task and returns a response
     *
     * @param task The task to process
     * @return A CompletableFuture containing the agent response
     */
    CompletableFuture<AgentResponse> processTask(String task);

    /**
     * Processes a task with WebSocket updates
     *
     * @param task    The task to process
     * @param session The WebSocket session
     * @return A CompletableFuture containing the agent response
     */
    CompletableFuture<AgentResponse> processTaskWithWebSocket(String task, WebSocketSession session);

    /**
     * Gets all responses for a session
     *
     * @param sessionId The session ID
     * @return A list of stream responses
     */
    List<StreamResponse> getResponses(String sessionId);
}