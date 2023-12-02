package com.example.demo.api.service.todo;

import com.example.demo.api.service.todo.model.TodoCreated;
import com.example.demo.api.service.todo.model.TodoItem;
import com.example.demo.api.service.todo.model.TodoUpdated;

import java.util.List;

public interface TodoService {
    TodoCreated createTodo(String message);
    List<TodoItem> getTodoList();
    TodoUpdated updateTodo(long id, boolean isDone);
    Long removeTodo(long id);
}
