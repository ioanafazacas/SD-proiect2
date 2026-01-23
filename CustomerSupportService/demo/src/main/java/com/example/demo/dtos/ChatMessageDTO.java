package com.example.demo.dtos;

public class ChatMessageDTO {
    private String userId;
    private String username;
    private String content;
    private boolean fromAdmin;

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isFromAdmin() { return fromAdmin; }
    public void setFromAdmin(boolean fromAdmin) { this.fromAdmin = fromAdmin; }
}
