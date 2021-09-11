package com.example.demospringamqppolling;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id = UUID.randomUUID().toString();
    private String target;
    private String content;
}
