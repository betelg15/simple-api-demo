package com.example.demo.persistence.repository;

import com.example.demo.infra.db.QueryDslConfig;
import com.example.demo.persistence.repository.entity.TodoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

@Import({QueryDslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void findDone() {
        TodoEntity todoEntity = todoRepository.save(
                new TodoEntity(
                        null,
                        "New Topic",
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        List<TodoEntity> todoList = todoRepository.findDone();

        Assertions.assertTrue(todoEntity.isDone());
        Assertions.assertEquals(todoEntity.getMessage(), "New Topic");
    }
}