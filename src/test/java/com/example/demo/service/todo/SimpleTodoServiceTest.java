package com.example.demo.service.todo;

import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.entity.TodoEntity;
import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SimpleTodoServiceTest {

    @Autowired
    private SimpleTodoService simpleTodoService;

    @MockBean
    private TodoRepository todoRepository;

    @DisplayName("Todo를 생성할 수 있어야 한다")
    @Test
    void createTodo() {
        long todoId = 1L;
        String message = "New Topic";

        Mockito.when(
                todoRepository.save(
                        Mockito.any()
                )
        ).thenReturn(
                new TodoEntity(
                        todoId,
                        message,
                        false,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        TodoCreated todoCrated = simpleTodoService.createTodo(message);

        assertEquals(
                new TodoCreated(
                        todoId,
                        message
                ),
                todoCrated
        );
    }

    @DisplayName("Todo 목록을 조회할 수 있어야 한다")
    @Test
    void getTodoList() {
        long todoId = 1L;
        String message = "New Topic";
        LocalDateTime now = LocalDateTime.now();

        Mockito.when(
                todoRepository.findAll()
        ).thenReturn(
                new ArrayList<>() {{
                    add(
                            new TodoEntity(
                                    todoId,
                                    message,
                                    false,
                                    now,
                                    now
                            )
                    );
                }}
        );

        List<TodoItem> todoList = simpleTodoService.getTodoList();

        assertEquals(
                new ArrayList<>() {{
                    add(
                            new TodoItem(
                                    todoId,
                                    message,
                                    false,
                                    now,
                                    now
                            )
                    );
                }},
                todoList
        );
    }

    @DisplayName("Todo를 수정할 수 있어야 한다")
    @Test
    void updateTodo() {
        long todoId = 1L;
        String message = "New Topic";

        Mockito.when(
                todoRepository.findById(todoId)
        ).thenReturn(
                Optional.of(
                    new TodoEntity(
                            todoId,
                            message,
                            false,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    )
                )
        );

        Mockito.when(
                todoRepository.save(Mockito.any())
        ).thenReturn(
                new TodoEntity(
                        todoId,
                        message,
                        false,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        TodoUpdated updated = simpleTodoService.updateTodo(todoId, true);

        assertEquals(
                new TodoUpdated(
                        todoId,
                        false
                ),
                updated
        );
    }

    @DisplayName("Todo를 삭제할 수 있어야 한다")
    @Test
    void removeTodo() {
        long todoId = 1L;

        Mockito.doNothing().when(
                todoRepository
        ).deleteById(todoId);

        long removedId = simpleTodoService.removeTodo(todoId);

        assertEquals(todoId, removedId);
    }
}