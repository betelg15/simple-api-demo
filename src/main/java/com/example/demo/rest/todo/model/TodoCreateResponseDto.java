package com.example.demo.rest.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class TodoCreateResponseDto {
    private long id;
    private String message;
}
