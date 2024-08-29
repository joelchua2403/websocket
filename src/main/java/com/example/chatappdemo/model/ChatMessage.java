package com.example.chatappdemo.model;
// Java library for JSON serialization and deserialization 
import com.google.gson.Gson;

public class ChatMessage {

    // Enum to represent the type of message
    public enum MessageType {
        CHAT,
        JOIN
    }

    // Fields
    private MessageType type;
    private String content;
    private String sender;

    // Getters and Setters
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    // Method to create a ChatMessage object from a JSON string
    public static ChatMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ChatMessage.class);
    }

    // Method to convert a ChatMessage object to a JSON string
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
