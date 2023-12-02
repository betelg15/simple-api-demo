package com.example.demo.service.todo;

import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.entity.TodoEntity;
import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleTodoService implements TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    @Override
    public TodoCreated createTodo(String message) {
        TodoEntity saved = todoRepository.save(
                new TodoEntity(
                        null,
                        message,
                        false,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        return new TodoCreated(
                saved.getId(),
                saved.getMessage()
        );
    }

    @Cacheable(value = "todos")
    @Override
    public List<TodoItem> getTodoList() {
        return todoRepository.findAll()
                .stream().map(TodoItem::valueOf).toList();
    }

    @Override
    public TodoUpdated updateTodo(long id, boolean isDone) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow();
        todo.setDone(isDone);
        TodoEntity saved = todoRepository.save(todo);

        return new TodoUpdated(
                saved.getId(),
                saved.isDone()
        );
    }

    @Override
    public Long removeTodo(long id) {
        todoRepository.deleteById(id);
        return id;
    }
}
