package com.example.demo.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String DEVICE_DATA_QUEUE = "device.data.queue";
    public static final String SYNC_QUEUE = "synchronization.queue";

    // Exchange names
    public static final String DEVICE_DATA_EXCHANGE = "device.data.exchange";
    public static final String SYNC_EXCHANGE = "synchronization.exchange";

    // Routing keys
    public static final String DEVICE_DATA_ROUTING_KEY = "device.data";
    public static final String SYNC_ROUTING_KEY = "sync.event";

    // Device Data Queue Configuration
    @Bean
    public Queue deviceDataQueue() {
        return QueueBuilder.durable(DEVICE_DATA_QUEUE)
                .build();
    }

    @Bean
    public TopicExchange deviceDataExchange() {
        return new TopicExchange(DEVICE_DATA_EXCHANGE);
    }

    @Bean
    public Binding deviceDataBinding() {
        return BindingBuilder
                .bind(deviceDataQueue())
                .to(deviceDataExchange())
                .with(DEVICE_DATA_ROUTING_KEY);
    }

    // Synchronization Queue Configuration
    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(SYNC_QUEUE)
                .build();
    }

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Binding syncBinding() {
        return BindingBuilder
                .bind(syncQueue())
                .to(syncExchange())
                .with(SYNC_ROUTING_KEY);
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}