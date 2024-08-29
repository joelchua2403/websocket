package com.example.chatappdemo.config;

import com.example.chatappdemo.controller.ChatController;
import com.example.chatappdemo.service.ChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration // Marks this class as a configuration class
@EnableWebSocket // Enables WebSocket support
public class WebSocketConfig implements WebSocketConfigurer {

    // ChstService bean
    private final ChatService chatService;

    // Constructor to inject the ChatService bean
    public WebSocketConfig(ChatService chatService) {
        this.chatService = chatService;
    }

    // Register the ChatController bean with the WebSocketHandlerRegistry
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatController(), "/chat").setAllowedOrigins("*");
    }

    // Create a ChatController bean
    // Inject the ChatService bean into the ChatController bean
    @Bean
    public ChatController chatController() {
        return new ChatController(chatService);
    }
}
