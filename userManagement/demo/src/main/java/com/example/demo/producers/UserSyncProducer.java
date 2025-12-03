package com.example.demo.producers;


import com.example.demo.dtos.UserSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSyncProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.device}")
    private String exchangeName;


    @Value("${rabbitmq.routing.key.user}")
    private String syncRoutingKey;

    public UserSyncProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUser(UserSyncDTO dto) {
        rabbitTemplate.convertAndSend(
                exchangeName,
                syncRoutingKey,
                dto
        );
    }


}