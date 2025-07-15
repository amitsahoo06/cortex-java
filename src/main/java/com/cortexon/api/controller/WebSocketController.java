package com.cortexon.api.controller;

import com.cortexon.api.service.OrchestratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket controller for real-time communication
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketController extends TextWebSocketHandler {

    private final OrchestratorService orchestratorService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connection established: {}", session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            log.info("Received WebSocket message: {}", payload);
            
            // Process the task
            orchestratorService.processTaskWithWebSocket(payload, session);
        } catch (Exception e) {
            log.error("Error handling WebSocket message", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WebSocket connection closed: {}", session.getId());
        sessions.remove(session.getId());
    }
}