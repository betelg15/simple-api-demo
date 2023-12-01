package com.example.demo.rest.todo;

import com.example.demo.rest.model.TodoCreateRequestDto;
import com.example.demo.rest.model.TodoUpdateRequestDto;
import com.example.demo.service.todo.TodoService;
import com.example.demo.service.todo.model.TodoCreated;
import com.example.demo.service.todo.model.TodoItem;
import com.example.demo.service.todo.model.TodoUpdated;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @DisplayName("Todo 항목을 생성할 수 있어야 한다.")
    @Test
    void createTodo() throws Exception {
        Mockito.when(
                todoService.createTodo("New Todo")
        ).thenReturn(
                new TodoCreated(
                        1,
                        "New Todo"
                )
        );

        TodoCreateRequestDto requestDto = new TodoCreateRequestDto();
        requestDto.setMessage("New Todo");

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("New Todo"));
    }

    @DisplayName("Todo 목록을 조회할 수 있어야 한다.")
    @Test
    void getTodoList() throws Exception {
        Mockito.when(
                todoService.getTodoList()
        ).thenReturn(
                new ArrayList<>() {{
                    add(
                            new TodoItem(
                                   1,
                                    "New Todo",
                                    false,
                                    LocalDateTime.parse("2023-12-25T23:12:25"),
                                    LocalDateTime.parse("2023-12-25T23:12:25")
                            )
                    );
                }}
        );

        mockMvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message").value("New Todo"))
                .andExpect(jsonPath("$[0].isDone").value(false))
                .andExpect(jsonPath("$[0].createdAt").value("2023-12-25T23:12:25Z"))
                .andExpect(jsonPath("$[0].updatedAt").value("2023-12-25T23:12:25Z"));
    }

    @DisplayName("Todo 를 수정할 수 있어야 한다.")
    @Test
    void updateTodo() throws Exception {
        long todoId = 1L;

        Mockito.when(
                todoService.updateTodo(todoId, true)
        ).thenReturn(
                new TodoUpdated(
                       todoId, true
                )
        );

        TodoUpdateRequestDto requestDto = new TodoUpdateRequestDto();
        requestDto.setIsDone(true);

        mockMvc.perform(patch("/api/v1/todos/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.isDone").value(true));
    }

    @DisplayName("Todo 를 삭제할 수 있어야 한다.")
    @Test
    void removeTodo() throws Exception {
        long todoId = 1L;

        Mockito.when(
                todoService.removeTodo(todoId)
        ).thenReturn(
                todoId
        );

        mockMvc.perform(delete("/api/v1/todos/{id}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId));
    }
}