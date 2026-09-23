package com.todoapp.tasks.internal.controller;

import com.todoapp.tasks.internal.model.TodoTask;
import com.todoapp.tasks.internal.service.TodoTaskService;
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
public class TodoTaskController {

    private final TodoTaskService todoTaskService;

    // api = endpoint
    @GetMapping("")
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return ResponseEntity.ok(todoTaskService.getAllTasks());
    }

    @PostMapping("")
    public ResponseEntity<Void> addTask(@RequestBody TodoTask todoTask) {
        todoTaskService.saveTask(todoTask);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoTask> findTaskById(@PathVariable UUID id){
        Optional<TodoTask> existingTask = todoTaskService.findTaskById(id);
        if(existingTask.isPresent()) {
            return ResponseEntity.ok(existingTask.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id){
        Optional<TodoTask> existingTask = todoTaskService.findTaskById(id);
        if(existingTask.isPresent()) {
         todoTaskService.deleteTask(id);
         return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
