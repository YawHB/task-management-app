package com.todoapp.task.internal.service;

import com.todoapp.task.internal.model.Task;
import com.todoapp.task.internal.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public void saveTask(Task task){
        taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Optional<Task> findTaskById(UUID id) {
       return taskRepository.findById(id);
    }

    public void deleteTask(UUID id) {
         taskRepository.deleteById(id);
    }

}
