package com.example.demospringamqppolling;

import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static final Random random = new Random(10);

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private QueueService queueService;

    @Autowired
    private TargetRepository targetRepository;

    public Message createMessage(MessageDto messageDto) throws JsonProcessingException, AmqpException {
        Message message = new Message();
        message.setTarget(messageDto.getTarget());
        message.setContent(messageDto.getContent());

        // create queue dinamically if not exists
        queueService.prepareQueue(message.getTarget(), message.getTarget());

        // add target to repository if not exists
        targetRepository.createTarget(message.getTarget());

        // publish message to corresponding target queue
        rabbitTemplate.convertAndSend(directExchange.getName(), message.getTarget(),
                new ObjectMapper().writeValueAsString(message));
        return message;
    }

    public void processMessage(Message message) throws Exception {
        int randomValue = random.nextInt();
        if (randomValue % 2 == 0) {
            throw new Exception("Simulated error launched, for message:" + message.getId());
        }

        System.out.println(message);
    }
}
