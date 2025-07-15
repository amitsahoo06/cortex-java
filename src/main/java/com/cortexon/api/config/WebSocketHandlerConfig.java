package com.cortexon.api.config;

import com.cortexon.api.controller.WebSocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket handler configuration
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketHandlerConfig implements WebSocketConfigurer {

    private final WebSocketController webSocketController;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketController, "/ws")
                .setAllowedOrigins("*");
    }
}