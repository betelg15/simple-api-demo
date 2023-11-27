package com.example.demo.repository;

import com.example.demo.repository.entity.TodoEntity;

import java.util.List;

public interface TodoQueryDslRepository {
    List<TodoEntity> findDone();
}
