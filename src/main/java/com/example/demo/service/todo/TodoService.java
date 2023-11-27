package com.example.demo.service.todo;

import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;

import java.util.List;

public interface TodoService {
    TodoCreated createTodo(String message);
    List<TodoItem> getTodoList();
    TodoUpdated updateTodo(long id, boolean isDone);
    Long removeTodo(long id);
}
