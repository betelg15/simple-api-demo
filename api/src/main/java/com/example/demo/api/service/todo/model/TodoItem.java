package com.example.demo.api.service.todo.model;

import com.example.demo.persistence.repository.entity.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Getter
public class TodoItem implements Serializable {
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
