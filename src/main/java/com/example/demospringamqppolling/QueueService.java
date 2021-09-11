package com.example.demospringamqppolling;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private DirectExchange messagesExchange;
    
    private Map<String, String> queuesMap = new HashMap<>();

    public void prepareQueue(String queueName, String routingKey){
        if(queuesMap.get(queueName) == null){
            if(rabbitAdmin.getQueueInfo(queueName) == null){
                Queue queue = new Queue(queueName);
                rabbitAdmin.declareQueue(queue);
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(messagesExchange).with(routingKey));
            }
            
            queuesMap.put(queueName, routingKey);
        }
    }
}
