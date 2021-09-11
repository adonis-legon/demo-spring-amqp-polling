package com.example.demospringamqppolling;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public DirectExchange messagesExchange() {
        return new DirectExchange(messageConfig.getExchangeName());
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(cachingConnectionFactory);
    }
}
