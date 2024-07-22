package com.Backend.ToothDay.websocket;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established with session ID: " + session.getId());
        String authToken = null;
        List<String> authHeaders = session.getHandshakeHeaders().get("Authorization");

        if (authHeaders != null && !authHeaders.isEmpty()) {
            authToken = authHeaders.get(0).replace("Bearer ", "");
        }

        if (authToken != null) {
            try {
                Long userId = JwtUtil.getUserIdFromToken(authToken);
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                session.getAttributes().put("userId", userId);
                sessions.put(userId, session);
                System.out.println("Session stored for userId: " + userId);
            } catch (Exception e) {
                System.out.println("Invalid token: " + authToken);
                session.close(CloseStatus.BAD_DATA);
            }
        } else {
            System.out.println("Authorization header not found.");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // Handle other types of messages here
        session.sendMessage(new TextMessage("Echo: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("User disconnected: " + userId);
        } else {
            System.out.println("Session closed but userId not found in session attributes.");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Transport error: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }

    public void sendNotification(Long userId, String type, Long postId, String postTitle, String username) {
        WebSocketSession session = sessions.get(userId);
        System.out.println("Attempting to send notification to user " + userId);
        if (session != null && session.isOpen()) {
            try {
                ObjectNode notificationMessage = objectMapper.createObjectNode();
                notificationMessage.put("type", type);
                notificationMessage.put("postId", postId);
                notificationMessage.put("postTitle", postTitle);
                notificationMessage.put("username", username);

                String notificationString = objectMapper.writeValueAsString(notificationMessage);

                session.sendMessage(new TextMessage(notificationString));
                System.out.println("Notification sent to user " + userId + ": " + notificationString);
            } catch (Exception e) {
                System.err.println("Error sending message to user " + userId + ": " + e.getMessage());
            }
        } else {
            System.out.println("No open session found for user " + userId);
        }
    }}
