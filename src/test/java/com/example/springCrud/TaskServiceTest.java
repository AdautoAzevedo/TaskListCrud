package com.example.springCrud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springCrud.dtos.TaskRecordDto;
import com.example.springCrud.model.Task;
import com.example.springCrud.model.User;
import com.example.springCrud.model.UserPrincipal;
import com.example.springCrud.repositories.TaskRepository;
import com.example.springCrud.services.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testCreateTask() {
        UserPrincipal userPrincipal = new UserPrincipal(new User());
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(new Task(), userPrincipal);
        assertNotNull(createdTask);
        assertEquals(task, createdTask);
    }

    @Test
    public void testGetTasksForUser() {
        UserPrincipal userPrincipal = new UserPrincipal(new User());
        List<Task> tasks = new ArrayList<>();

        when(taskRepository.findByUser(any(User.class))).thenReturn(tasks);

        ResponseEntity<List<Task>> responseEntity = taskService.getTasksForUser(userPrincipal);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    public void testGetTestById() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<Task> responseEntity = taskService.getTaskById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(task, responseEntity.getBody());
    }

    @Test
    public void testDeleteTask() {
        UserPrincipal userPrincipal = new UserPrincipal(new User());
        
        Task task = new Task();
        task.setId(1L);
        task.setUser(userPrincipal.getUser());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<Object> responseEntity = taskService.deleteTask(1L, userPrincipal);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Task deleted sucessfully"));
    }


}