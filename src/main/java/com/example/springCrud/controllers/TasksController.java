package com.example.springCrud.controllers;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.springCrud.dtos.TaskRecordDto;
import com.example.springCrud.model.Task;
import com.example.springCrud.model.User;
import com.example.springCrud.model.UserPrincipal;
import com.example.springCrud.repositories.TaskRepository;

@RestController
public class TasksController {
    private TaskRepository taskRepository;

    @Autowired
    public TasksController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        UserPrincipal currentUserPrincipal = (UserPrincipal) authentication.getPrincipal();
    
        task.setUser(currentUserPrincipal.getUser());
        return taskRepository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(value="id") Long id) {
        Optional<Task> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
       
        return ResponseEntity.status(HttpStatus.OK).body(taskO.get());
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksForCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Task> tasks = taskRepository.findByUser(userPrincipal.getUser());

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable(value="id") Long id, @RequestBody TaskRecordDto taskRecordDto, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var taskModel = task.get();
        if (taskModel.getUser().getUser_id() != userPrincipal.getUser().getUser_id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this task.");
        }

        BeanUtils.copyProperties(taskRecordDto, taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskModel));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value="id") Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Optional<Task> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskO.get();
        if (task.getUser().getUser_id() != userPrincipal.getUser().getUser_id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this task.");
        }

        taskRepository.delete(taskO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }

}
