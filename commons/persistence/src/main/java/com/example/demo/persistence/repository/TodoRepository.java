package com.example.demo.persistence.repository;

import com.example.demo.persistence.repository.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long>, TodoQueryDslRepository {
}
