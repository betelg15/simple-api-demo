package com.example.demo.api.service.todo;

import com.example.demo.api.service.todo.model.TodoCreated;
import com.example.demo.api.service.todo.model.TodoItem;
import com.example.demo.api.service.todo.model.TodoUpdated;
import com.example.demo.persistence.repository.TodoRepository;
import com.example.demo.persistence.repository.entity.TodoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Cacheable(value = "todos")
    @Override
    public List<TodoItem> getTodoList() {
        return todoRepository.findAll()
                .stream().map(TodoItem::valueOf).toList();
    }

    @Transactional
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

    @Transactional
    @Override
    public Long removeTodo(long id) {
        todoRepository.deleteById(id);
        return id;
    }
}
