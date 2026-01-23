package com.example.demo.services;


import org.springframework.stereotype.Service;

@Service
public class RuleBasedResponder {

    public String respond(String userMessage) {
        userMessage = userMessage.toLowerCase();

        if (userMessage.contains("hours") || userMessage.contains("program")) {
            return "Our working hours are 9:00 - 17:00.";
        }
        if (userMessage.contains("price")) {
            return "Please check our pricing page at https://example.com/prices.";
        }
        if (userMessage.contains("support")) {
            return "You can contact our support team at support@example.com.";
        }

        return null; // fallback la AI
    }
}
