package com.example.chatappdemo.service;

import org.springframework.stereotype.Service;

import com.example.chatappdemo.model.ChatMessage;

@Service
public class ChatService {

    public ChatMessage processMessage(ChatMessage chatMessage) {
        // Add business logic if needed
        return chatMessage;
    }

    public ChatMessage processUserJoin(ChatMessage chatMessage) {
        chatMessage.setContent(chatMessage.getSender() + " joined the chat");
        return chatMessage;
    }
}
