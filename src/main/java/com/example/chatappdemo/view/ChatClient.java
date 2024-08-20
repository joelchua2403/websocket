package com.example.chatappdemo.view;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;

public class ChatClient extends JFrame {
    private JTextField messageField;
    private JTextArea chatArea;
    private WebSocketClient webSocketClient;
    private String username;

    public ChatClient(String username) {
        this.username = username;

        // Set up the JFrame
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Message input field and send button
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messageField = new JTextField();
        panel.add(messageField, BorderLayout.CENTER);
        JButton sendButton = new JButton("Send");
        panel.add(sendButton, BorderLayout.EAST);
        add(panel, BorderLayout.SOUTH);

        // Connect to the WebSocket server
        connectWebSocket();

        // Add send button action listener
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add message field action listener (pressing Enter sends the message)
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void connectWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/ws")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    chatArea.append("Connected to the server.\n");
                    webSocketClient.send("{\"type\":\"JOIN\",\"sender\":\"" + username + "\"}");
                }

                @Override
                public void onMessage(String message) {
                    chatArea.append("Message received: " + message + "\n");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    chatArea.append("Disconnected from the server. Reason: " + reason + "\n");
                }

                @Override
                public void onError(Exception ex) {
                    chatArea.append("Error: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException ex) {
            chatArea.append("Error: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    private void sendMessage() {
        String messageContent = messageField.getText().trim();
        if (!messageContent.isEmpty()) {
            webSocketClient.send("{\"type\":\"CHAT\",\"sender\":\"" + username + "\",\"content\":\"" + messageContent + "\"}");
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username != null && !username.trim().isEmpty()) {
            ChatClient client = new ChatClient(username);
            client.setVisible(true);
        }
    }
}
