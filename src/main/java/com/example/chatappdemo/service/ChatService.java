package com.example.chatappdemo.service;

import com.example.chatappdemo.model.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Marks this class as a service, making it a Spring-managed bean
@Service
public class ChatService {

    // Set to store WebSocket sessions
    private Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Method to add a WebSocket session to the set
    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    // Method to remove a WebSocket session from the set
    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // Method to process a message received from a WebSocket session
    public void handleMessage(WebSocketSession session, String message) {
        try {
            // Convert the JSON message to a ChatMessage object
            ChatMessage chatMessage = ChatMessage.fromJson(message);
            // Check the message type and broadcast the message to all sessions
            if (chatMessage.getType() == ChatMessage.MessageType.JOIN) {
                broadcastMessage(session, chatMessage.getSender() + " joined the chat");
            } else if (chatMessage.getType() == ChatMessage.MessageType.CHAT) {
                broadcastMessage(session, chatMessage.getSender() + ": " + chatMessage.getContent());
            }
        } catch (Exception e) {
            // If there is an error processing the message, send an error message to the client
            try {
                session.sendMessage(new TextMessage("Error processing message: " + e.getMessage()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to broadcast a message to all WebSocket sessions except the sender
    private void broadcastMessage(WebSocketSession sender, String message) throws Exception {
        for (WebSocketSession session : sessions) {
            if (!session.equals(sender)) {
                session.sendMessage(new TextMessage(message));
            }
        }
        // Also send the message to the sender
        sender.sendMessage(new TextMessage(message));
    }
}
