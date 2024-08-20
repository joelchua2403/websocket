package com.example.chatappdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.chatappdemo.model.ChatMessage;
import com.example.chatappdemo.service.ChatService;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatService.processMessage(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage) {
        return chatService.processUserJoin(chatMessage);
    }
}
