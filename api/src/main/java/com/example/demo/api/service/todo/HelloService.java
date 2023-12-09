package com.example.demo.api.service.todo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public String hello(String name) {
        kafkaTemplate.send("hello-topic", name);
        return name + " sent 'hello' to Jack";
    }
}