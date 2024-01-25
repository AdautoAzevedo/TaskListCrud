package com.example.springCrud.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.springCrud.dtos.TaskRecordDto;
import com.example.springCrud.model.Task;
import com.example.springCrud.model.UserPrincipal;
import com.example.springCrud.services.TaskService;

@RestController
public class TasksController {
    private TaskService taskService;

    @Autowired
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        UserPrincipal currentUserPrincipal = (UserPrincipal) authentication.getPrincipal();
        return taskService.createTask(task, currentUserPrincipal);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(value="id") Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksForCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return taskService.getTasksForUser(userPrincipal);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable(value="id") Long id, @RequestBody TaskRecordDto taskRecordDto, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return taskService.updateTask(id, taskRecordDto, userPrincipal);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value="id") Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return taskService.deleteTask(id, userPrincipal);
    }
}
