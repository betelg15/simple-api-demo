package com.example.demo.rest.todo;

import com.example.demo.rest.todo.model.*;
import com.example.demo.service.todo.TodoService;
import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public TodoCreateResponseDto createTodo(@RequestBody TodoCreateRequestDto todoCreateRequestDto) {
        TodoCreated created = todoService.createTodo(todoCreateRequestDto.getMessage());
        return new TodoCreateResponseDto(created.getId(), created.getMessage());
    }

    @GetMapping("/todos")
    public List<TodoItemResponseDto> getTodoList()  {
        List<TodoItem> todoList = todoService.getTodoList();

        return todoList.stream()
                .map(TodoItemResponseDto::valueOf)
                .collect(Collectors.toList());
    }

    @PatchMapping("/todos/{id}")
    public TodoUpdateResponseDto updateTodo(
            @PathVariable("id") Long id,
            @RequestBody TodoUpdateRequestDto todoUpdateRequestDto
    ) {
        TodoUpdated updated = todoService.updateTodo(id, todoUpdateRequestDto.isDone());
        return new TodoUpdateResponseDto(updated.getId(), updated.isDone());
    }

    @DeleteMapping("/todos/{id}")
    public TodoRemoveResponseDto removeTodo(@PathVariable("id") Long id) {
        Long removedId = todoService.removeTodo(id);
        return new TodoRemoveResponseDto(removedId);
    }
}
