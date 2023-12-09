package com.example.demo.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloConsumer {

    @KafkaListener(topics = "hello-topic", groupId = "my-group")
    public void consumer(ConsumerRecord<String, String> consumerRecord) {
        String name = consumerRecord.value();
        log.info("Oh, I'm here. Get in! " + name);
    }
}