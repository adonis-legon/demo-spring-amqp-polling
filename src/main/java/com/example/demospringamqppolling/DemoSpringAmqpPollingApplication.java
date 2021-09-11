package com.example.demospringamqppolling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoSpringAmqpPollingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringAmqpPollingApplication.class, args);
	}

}
