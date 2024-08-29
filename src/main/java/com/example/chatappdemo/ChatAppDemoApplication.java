package com.example.chatappdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatAppDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppDemoApplication.class, args);
        System.out.println("WebSocket server started on ws://localhost:8080/chat");
    }
}
