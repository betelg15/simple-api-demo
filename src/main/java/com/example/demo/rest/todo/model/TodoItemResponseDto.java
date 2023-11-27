package com.example.demo.rest.todo.model;

import com.example.demo.service.todo.model.TodoItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class TodoItemResponseDto {
    private long id;
    private String message;

    @JsonProperty(value="isDone")
    private boolean isDone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoItemResponseDto valueOf(TodoItem todoItem) {
        TodoItemResponseDto dto = new TodoItemResponseDto();
        dto.setId(todoItem.getId());
        dto.setMessage(todoItem.getMessage());
        dto.setDone(todoItem.isDone());
        dto.setCreatedAt(todoItem.getCreatedAt());
        dto.setUpdatedAt(todoItem.getUpdatedAt());
        return dto;
    }
}
