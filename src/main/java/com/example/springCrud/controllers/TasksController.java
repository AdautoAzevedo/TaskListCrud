package com.example.springCrud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springCrud.dtos.TaskRecordDto;
import com.example.springCrud.model.Task;
import com.example.springCrud.repositories.TaskRepository;

@RestController
public class TasksController {
    private TaskRepository taskRepository;

    @Autowired
    public TasksController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
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

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable(value="id") Long id, @RequestBody TaskRecordDto taskRecordDto) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var taskModel = task.get();
        BeanUtils.copyProperties(taskRecordDto, taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskModel));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value="id") Long id) {
        Optional<Task> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.delete(taskO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }

}
