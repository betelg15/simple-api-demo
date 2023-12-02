package com.example.demo.api.service.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class TodoCreated {
    private long id;
    private String message;
}
