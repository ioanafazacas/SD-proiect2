package com.example.demo.services;


import org.springframework.stereotype.Service;

@Service
public class AIResponse {

    public String respond(String message) {
        return "Thank you for your message. An operator will respond shortly.";
    }
}
