package com.sparta.aper_chat_back.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.aper_chat_back.chat.controller.StompRabbitController;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${local.rabbitmq.username}")
    private String rabbitMqUsername;

    @Value("${local.rabbitmq.password}")
    private String rabbitMqPassword;
    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private static final String ROUTING_KEY = "room.*";

    @Bean
    public Queue queue() {
        return new Queue(CHAT_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CHAT_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
//        return rabbitTemplate;
//    }

//    @Bean
//    public SimpleMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer containers = new SimpleMessageListenerContainer();
//        containers.setConnectionFactory(connectionFactory());
//        containers.setQueueNames(CHAT_QUEUE_NAME);
//        containers.setMessageListener(listenerAdapter); //containers.setMessageListener(null); 이라고 설정했더니 에러가 남.
//        return containers;
//    }

    @Bean
    public MessageListenerAdapter listenerAdapter(StompRabbitController stompRabbitController) {
        return new MessageListenerAdapter(stompRabbitController, "receive");
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost("localhost");
//        factory.setUsername(rabbitMqUsername);
//        factory.setPassword(rabbitMqPassword);
//        return factory;
//    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.registerModule(dateTimeModule());

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public JavaTimeModule dateTimeModule() {
        return new JavaTimeModule();
    }
}
