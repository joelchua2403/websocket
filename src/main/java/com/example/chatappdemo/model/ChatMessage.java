package com.example.chatappdemo.model;
import com.google.gson.Gson;

public class ChatMessage {

    public enum MessageType {
        CHAT,
        JOIN
    }

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

    // JSON Parsing
    public static ChatMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ChatMessage.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
