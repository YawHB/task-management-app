package com.todoapp.tasks.repository;

import com.todoapp.tasks.model.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, UUID> {

}
