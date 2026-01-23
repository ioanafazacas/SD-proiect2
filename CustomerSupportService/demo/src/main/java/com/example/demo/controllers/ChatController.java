package com.example.demo.controllers;


import com.example.demo.dtos.ChatMessageDTO;
import com.example.demo.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDTO message) {
        chatService.processMessage(message);
    }
}
