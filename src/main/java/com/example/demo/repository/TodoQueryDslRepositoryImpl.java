package com.example.demo.repository;

import com.example.demo.repository.entity.QTodoEntity;
import com.example.demo.repository.entity.TodoEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class TodoQueryDslRepositoryImpl implements TodoQueryDslRepository {

    private JPAQueryFactory jpaQueryFactory;
    private QTodoEntity todo = QTodoEntity.todoEntity;

    public TodoQueryDslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<TodoEntity> findDone() {
        return jpaQueryFactory.select(todo)
                .from(todo)
                .where(
                        todo.isDone.eq(true)
                ).fetch();
    }
}
