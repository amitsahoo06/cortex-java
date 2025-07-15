package com.cortexon.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Utility class for WebSocket operations
 */
@Slf4j
public class WebSocketUtil {

    private WebSocketUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sends a message through a WebSocket session
     *
     * @param session The WebSocket session
     * @param message The message to send
     * @return true if the message was sent successfully, false otherwise
     */
    public static boolean sendMessage(WebSocketSession session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                return true;
            } catch (IOException e) {
                log.error("Error sending WebSocket message", e);
            }
        }
        return false;
    }
}