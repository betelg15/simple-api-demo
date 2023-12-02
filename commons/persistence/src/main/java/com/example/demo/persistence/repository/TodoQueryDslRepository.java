package com.example.demo.persistence.repository;

import com.example.demo.persistence.repository.entity.TodoEntity;

import java.util.List;

public interface TodoQueryDslRepository {
    List<TodoEntity> findDone();
}
