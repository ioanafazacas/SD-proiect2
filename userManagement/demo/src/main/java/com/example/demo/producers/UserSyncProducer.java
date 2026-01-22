package com.example.demo.producers;

import com.example.demo.dtos.UserSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSyncProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserSyncProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUser(UserSyncDTO userSyncDTO) {
        try {
            rabbitTemplate.convertAndSend("user-sync-queue", userSyncDTO);
            LOGGER.info("✅ User sync message sent: {}", userSyncDTO);
        } catch (Exception e) {
            LOGGER.error("❌ Failed to send user sync message: {}", e.getMessage(), e);
        }
    }
}