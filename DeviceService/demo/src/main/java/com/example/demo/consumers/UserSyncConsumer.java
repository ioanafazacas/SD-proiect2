package com.example.demo.consumers;

import com.example.demo.dtos.UserSyncDTO;
import com.example.demo.entities.Users;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSyncConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncConsumer.class);

    private final UserRepository userRepository;

    @Autowired
    public UserSyncConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "user-sync-queue")
    public void consumeUserSync(UserSyncDTO userSyncDTO) {
        try {
            LOGGER.info("📥 Received user sync message: {}", userSyncDTO);

            switch (userSyncDTO.getOperation()) {
                case "CREATE":
                    Users newUser = new Users(userSyncDTO.getUserId());
                    userRepository.save(newUser);
                    LOGGER.info("✅ User created in DeviceService: {}", userSyncDTO.getUserId());
                    break;

                case "DELETE":
                    userRepository.deleteById(userSyncDTO.getUserId());
                    LOGGER.info("✅ User deleted from DeviceService: {}", userSyncDTO.getUserId());
                    break;

                default:
                    LOGGER.warn("⚠️ Unknown operation: {}", userSyncDTO.getOperation());
            }
        } catch (Exception e) {
            LOGGER.error("❌ Error processing user sync: {}", e.getMessage(), e);
        }
    }
}