package com.example.springCrud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.springCrud.dtos.TaskRecordDto;
import com.example.springCrud.model.Task;
import com.example.springCrud.model.UserPrincipal;
import com.example.springCrud.repositories.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task, UserPrincipal userPrincipal){
        task.setUser(userPrincipal.getUser());
        return taskRepository.save(task);
    }

    public ResponseEntity<Task> getTaskById(Long id) {
        Optional<Task> taskO = taskRepository.findById(id);
        return taskO.map(task -> ResponseEntity.status(HttpStatus.OK).body(task))
                .orElseGet(() -> ResponseEntity.notFound().build());   
    }

    public ResponseEntity<List<Task>> getTasksForUser(UserPrincipal userPrincipal) {
        List<Task> tasks = taskRepository.findByUser(userPrincipal.getUser());
        return ResponseEntity.ok(tasks);
    }

    public ResponseEntity updateTask(Long id, TaskRecordDto taskRecordDto, UserPrincipal userPrincipal) {
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

    public ResponseEntity<Object> deleteTask(Long id, UserPrincipal userPrincipal) {
        try {
            Optional<Task> taskO = taskRepository.findById(id);
            if (taskO.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Task task = taskO.get();

            if (task.getUser().getUser_id() != userPrincipal.getUser().getUser_id()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this task.");
            }

            taskRepository.delete(task);
            
            return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the task.");
        }        
    }
}
