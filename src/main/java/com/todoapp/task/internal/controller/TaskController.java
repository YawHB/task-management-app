package com.todoapp.task.internal.controller;

import com.todoapp.task.internal.model.Task;
import com.todoapp.task.internal.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
// RestController = Controller + ResponseBody
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // api = endpoint
    @GetMapping("")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("")
    public ResponseEntity<Void> addTask(@RequestBody Task task) {
        taskService.saveTask(task);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable UUID id){
        Optional<Task> existingTask = taskService.findTaskById(id);
        if(existingTask.isPresent()) {
            return ResponseEntity.ok(existingTask.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id){
        Optional<Task> existingTask = taskService.findTaskById(id);
        if(existingTask.isPresent()) {
         taskService.deleteTask(id);
         return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
