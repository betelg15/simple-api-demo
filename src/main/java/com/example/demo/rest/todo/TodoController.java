package com.example.demo.rest.todo;

import com.example.demo.rest.TodoApi;
import com.example.demo.rest.model.*;
import com.example.demo.service.todo.TodoService;
import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoController implements TodoApi {

    private final TodoService todoService;

    @Override
    public ResponseEntity<TodoCreateResponseDto> createTodo(TodoCreateRequestDto todoCreateRequestDto) {
        TodoCreated created = todoService.createTodo(todoCreateRequestDto.getMessage());
        TodoCreateResponseDto responseBody = new TodoCreateResponseDto(created.getId(), created.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TodoItemResponseDto>> getTodoList() {
        List<TodoItem> todoList = todoService.getTodoList();

        List<TodoItemResponseDto> responseBody = todoList.stream()
                .map(TodoController::convertToTodoItemResponseDto)
                .toList();

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TodoRemoveResponseDto> removeTodo(Long id) {
        Long removedId = todoService.removeTodo(id);
        TodoRemoveResponseDto responseBody = new TodoRemoveResponseDto(removedId);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

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
