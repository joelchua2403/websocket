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
    // GUI components
    private JTextField messageField; // Input field for messages
    private JTextArea chatArea; // Area to display chat messages
    private WebSocketClient webSocketClient; // WebSocket client
    private String username; // Username of the client

    // Constructor
    public ChatClient(String username) {
        this.username = username;

        // Set up the JFrame
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Make the chat area read-only
        add(new JScrollPane(chatArea), BorderLayout.CENTER); // Add the scrollable chat area to the frame

        // Message input field and send button
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messageField = new JTextField();
        panel.add(messageField, BorderLayout.CENTER); // Message input field in the center of the panel
        JButton sendButton = new JButton("Send");
        panel.add(sendButton, BorderLayout.EAST); // Send button on the right side of the panel
        add(panel, BorderLayout.SOUTH); // Add the panel to the bottom of the frame

        // Connect to the WebSocket server
        connectWebSocket();

        // Add send button action listener
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(); // Call the sendMessage method when the button is clicked
            }
        });

        // Add message field action listener (pressing Enter sends the message)
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(); // Call the sendMessage method when Enter is pressed in the message field
            }
        });
    }

    // Method to connect to the WebSocket server
    private void connectWebSocket() {
        try {
            // Create a new WebSocket client and connect to the server
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/chat")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    // Send a JOIN message to the server when the connection is established
                    chatArea.append("Connected to the server.\n");
                    webSocketClient.send("{\"type\":\"JOIN\",\"sender\":\"" + username + "\"}");
                }

                @Override
                public void onMessage(String message) {
                    // Display the received message in the chat area
                    chatArea.append("Message received: " + message + "\n");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    // Display a message when the connection is closed
                    chatArea.append("Disconnected from the server. Reason: " + reason + "\n");
                }

                @Override
                public void onError(Exception ex) {
                    // Display an error message if an error occurs
                    chatArea.append("Error: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect(); // Connect to the WebSocket server
        } catch (URISyntaxException ex) {
            // Display an error message if the URI is invalid
            chatArea.append("Error: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    // Method to send a message to the WebSocket server
    private void sendMessage() {
        String messageContent = messageField.getText().trim(); // Get the message content from the input field and remove leading/trailing whitespace
        if (!messageContent.isEmpty()) {
            // if the message content is not empty, send a CHAT message to the server
            webSocketClient.send("{\"type\":\"CHAT\",\"sender\":\"" + username + "\",\"content\":\"" + messageContent + "\"}");
            messageField.setText("");
        }
    }

    // Main method to create a ChatClient instance
    public static void main(String[] args) {
        // Prompt the user to enter a username
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username != null && !username.trim().isEmpty()) {
            // if the username is not empty, create a ChatClient instance with the entered username
            ChatClient client = new ChatClient(username);
            client.setVisible(true);
        }
    }
}
