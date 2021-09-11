package com.example.demospringamqppolling;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private QueueService queueService;

    @PostConstruct
    private void init(){
        targetRepository.getAll().forEach(target -> queueService.prepareQueue(target, target));
    }

    @Scheduled(fixedDelayString = "#{messageConfig.getPollInterval()}")
    public void pollQueue() throws JsonMappingException, JsonProcessingException {
        rabbitTemplate.execute(channel -> processRabbitChannel(channel));
    }

    private Boolean processRabbitChannel(final Channel channel) throws Exception {
        for (String target : targetRepository.getAll()) {
            int currentBatchCount = Integer.parseInt(messageConfig.getBatchSize());
            long deliveryTag = 0;

            channel.basicQos(currentBatchCount); // prefetch
            while (currentBatchCount > 0) {
                GetResponse result = channel.basicGet(target, false);

                if (result != null) {
                    Message message = new ObjectMapper().readValue(new String(result.getBody()), Message.class);
                    deliveryTag = result.getEnvelope().getDeliveryTag();

                    if (deliveryTag > 0) {
                        try {
                            messageService.processMessage(message);
                            channel.basicAck(deliveryTag, false);
                        } catch (Exception e) {
                            channel.basicReject(deliveryTag, true);
                            System.err.println(String.format("Error processing message %s from queue %s",
                                    message.getId(), target));
                        }
                    }

                    currentBatchCount--;
                } else {
                    System.out.println(String.format("Queue %s is empty", target));
                    break;
                }
            }
        }
        return true;
    }
}
