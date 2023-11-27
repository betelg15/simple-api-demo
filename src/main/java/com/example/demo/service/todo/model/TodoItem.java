package com.example.demo.service.todo.model;

import com.example.demo.repository.entity.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Getter
public class TodoItem {
    private long id;
    private String message;
    private boolean isDone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoItem valueOf(TodoEntity todoEntity) {
        return new TodoItem(
                todoEntity.getId(),
                todoEntity.getMessage(),
                todoEntity.isDone(),
                todoEntity.getCreatedAt(),
                todoEntity.getUpdatedAt()
        );
    }
}
