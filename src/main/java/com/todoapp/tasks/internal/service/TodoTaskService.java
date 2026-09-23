package com.todoapp.tasks.internal.service;

import com.todoapp.tasks.internal.model.TodoTask;
import com.todoapp.tasks.internal.repository.TodoTaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;

    public void saveTask(TodoTask todoTask){
        todoTaskRepository.save(todoTask);
    }

    public List<TodoTask> getAllTasks(){
        return todoTaskRepository.findAll();
    }

    public Optional<TodoTask> findTaskById(UUID id) {
       return todoTaskRepository.findById(id);
    }

    public void deleteTask(UUID id) {
         todoTaskRepository.deleteById(id);
    }

}
