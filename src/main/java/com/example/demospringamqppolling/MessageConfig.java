package com.example.demospringamqppolling;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "messages")
@Data
public class MessageConfig {
    private String exchangeName;
    private String batchSize;
    private String pollInterval;
}
