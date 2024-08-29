package com.example.chatappdemo.controller;

import com.example.chatappdemo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller // Marks this class as a controller bean
public class ChatController extends TextWebSocketHandler {

    // ChatService bean
    private final ChatService chatService;

    // Constructor to inject the ChatService bean when creating a ChatController bean
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    //  Method called when a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Retrives the client's IP address and port number
        String clientInfo = session.getRemoteAddress().getAddress().getHostAddress() + ":" + session.getRemoteAddress().getPort();
        System.out.println("New connection from " + clientInfo);
        // Adds the session to the ChatService
        chatService.addSession(session);
        // Sends a message to the client
        session.sendMessage(new TextMessage("Connected to the server from " + clientInfo));
    }

    // Method called when a new message is received from a WebSocket connection
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message from " + session.getRemoteAddress() + ": " + message.getPayload());
        // Passes the message to the ChatService for processing
        chatService.handleMessage(session, message.getPayload());
    }

    // Method called when a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Removes the session from the ChatService
        chatService.removeSession(session);
        System.out.println("Closed connection to " + session.getRemoteAddress());
    }
}
