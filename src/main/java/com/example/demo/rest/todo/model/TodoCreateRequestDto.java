package com.example.demo.rest.todo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TodoCreateRequestDto {
    private String message;
}
