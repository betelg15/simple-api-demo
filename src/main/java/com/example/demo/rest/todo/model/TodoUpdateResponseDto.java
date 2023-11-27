package com.example.demo.rest.todo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class TodoUpdateResponseDto {
    private long id;

    @JsonProperty("isDone")
    private boolean isDone;
}
