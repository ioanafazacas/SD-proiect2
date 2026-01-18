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

    // Exchange names
    public static final String DEVICE_DATA_EXCHANGE = "device.data.exchange";

    // Routing keys
    public static final String DEVICE_DATA_ROUTING_KEY = "device.data";


    public static final String QUEUE_1 = "monitoring_ingest_1";
    public static final String QUEUE_2 = "monitoring_ingest_2";
    public static final String QUEUE_3 = "monitoring_ingest_3";


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

    @Bean
    public Queue ingestQueue1() {
        return new Queue(QUEUE_1, true);
    }

    @Bean
    public Queue ingestQueue2() {
        return new Queue(QUEUE_2, true);
    }

    @Bean
    public Queue ingestQueue3() {
        return new Queue(QUEUE_3, true);
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