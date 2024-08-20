package com.example.chatappdemo.view;
import org.java_websocket.server.WebSocketServer;

import com.example.chatappdemo.model.ChatMessage;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWebSocketServer extends WebSocketServer {

    private Set<WebSocket> connections = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public SimpleWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ": " + message);
        handleMessage(conn, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully!");
    }

    private void handleMessage(WebSocket conn, String message) {
        // Parse the message and handle based on its type
        try {
            ChatMessage chatMessage = ChatMessage.fromJson(message);
            if (chatMessage.getType() == ChatMessage.MessageType.JOIN) {
                broadcastMessage(conn, chatMessage.getSender() + " joined the chat");
            } else if (chatMessage.getType() == ChatMessage.MessageType.CHAT) {
                broadcastMessage(conn, chatMessage.getSender() + ": " + chatMessage.getContent());
            }
        } catch (Exception e) {
            conn.send("Error processing message: " + e.getMessage());
        }
    }

    private void broadcastMessage(WebSocket sender, String message) {
        for (WebSocket conn : connections) {
            if (conn != sender) {
                conn.send(message);
            }
        }
        // Also send the message back to the sender
        sender.send(message);
    }

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        SimpleWebSocketServer server = new SimpleWebSocketServer(address);
        server.start();
        System.out.println("WebSocket server started on ws://localhost:8080");
    }
}
