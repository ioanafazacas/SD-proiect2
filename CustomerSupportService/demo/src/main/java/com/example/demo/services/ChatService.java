package com.example.demo.services;

import com.example.demo.dtos.ChatMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RuleBasedResponder ruleResponder;
    private final AIResponse aiResponder;

    public ChatService(SimpMessagingTemplate messagingTemplate, RuleBasedResponder ruleResponder, AIResponse aiResponder) {
        this.messagingTemplate = messagingTemplate;
        this.ruleResponder = ruleResponder;
        this.aiResponder = aiResponder;
    }

    public void processMessage(ChatMessageDTO message) {

        // 1️⃣ Trimitem mesajul original către admin
        messagingTemplate.convertAndSend(
                "/topic/admin",
                message
        );

        // 2️⃣ Rule-based auto reply
        String response = ruleResponder.respond(message.getContent());

        if (response != null) {
            ChatMessageDTO reply = new ChatMessageDTO();
            reply.setContent(response);
            reply.setFromAdmin(true);
            reply.setUserId(message.getUserId());

            messagingTemplate.convertAndSend(
                    "/topic/" + message.getUserId(),
                    reply
            );
            return;
        }else{
            response = aiResponder.respond(message.getContent());

        }

        // 3️⃣ AI fallback (opțional)
    }

//    public void processMessage(ChatMessageDTO message) {
//        // Verificăm dacă mesajul poate fi răspuns de Rule-Based
//        String response = ruleResponder.respond(message.getContent());
//        if (response != null) {
//            ChatMessageDTO reply = new ChatMessageDTO();
//            reply.setContent(response);
//            reply.setFromAdmin(true);
//            reply.setUserId(message.getUserId());
//            messagingTemplate.convertAndSend("/topic/" + message.getUserId(), reply);
//        } else {
//            // fallback AI (apel la API extern)
//            // response = AIResponder.generateResponse(message.getContent());
//        }
//    }
}
