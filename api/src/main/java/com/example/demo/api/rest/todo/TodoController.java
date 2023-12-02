package com.example.demo.api.rest.todo;

import com.example.demo.api.rest.TodoApi;
import com.example.demo.api.rest.model.*;
import com.example.demo.api.service.todo.TodoService;
import com.example.demo.api.service.todo.model.TodoCreated;
import com.example.demo.api.service.todo.model.TodoItem;
import com.example.demo.api.service.todo.model.TodoUpdated;
import com.example.demo.core.annotation.security.IsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoController implements TodoApi {

    private final TodoService todoService;

    @IsUser
    @Override
    public ResponseEntity<TodoCreateResponseDto> createTodo(TodoCreateRequestDto todoCreateRequestDto) {
        TodoCreated created = todoService.createTodo(todoCreateRequestDto.getMessage());
        TodoCreateResponseDto responseBody = new TodoCreateResponseDto(created.getId(), created.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @IsUser
    @Override
    public ResponseEntity<List<TodoItemResponseDto>> getTodoList() {
        SecurityContext sc = SecurityContextHolder.getContext();
        List<TodoItem> todoList = todoService.getTodoList();

        List<TodoItemResponseDto> responseBody = todoList.stream()
                .map(TodoController::convertToTodoItemResponseDto)
                .toList();

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @IsUser
    @Override
    public ResponseEntity<TodoRemoveResponseDto> removeTodo(Long id) {
        Long removedId = todoService.removeTodo(id);
        TodoRemoveResponseDto responseBody = new TodoRemoveResponseDto(removedId);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @IsUser
    @Override
    public ResponseEntity<TodoUpdateResponseDto> updateTodo(Long id, TodoUpdateRequestDto todoUpdateRequestDto) {
        TodoUpdated updated = todoService.updateTodo(id, todoUpdateRequestDto.getIsDone());
        TodoUpdateResponseDto responseBody = new TodoUpdateResponseDto(updated.getId(), updated.isDone());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private static TodoItemResponseDto convertToTodoItemResponseDto(TodoItem item) {
        return new TodoItemResponseDto(
                item.getId(),
                item.getMessage(),
                item.isDone(),
                item.getCreatedAt().atOffset(ZoneOffset.UTC),
                item.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
