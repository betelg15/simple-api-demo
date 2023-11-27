package com.example.demo.repository;

import com.example.demo.repository.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long>, TodoQueryDslRepository {
}
