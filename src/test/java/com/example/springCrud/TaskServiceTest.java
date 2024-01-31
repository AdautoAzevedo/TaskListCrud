package com.example.springCrud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    private User testUser;
    private Task testTask;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUser_id(1);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setUser(testUser);
    }
    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task createdTask = taskService.createTask(new Task(), new UserPrincipal(testUser));
        assertNotNull(createdTask);
        assertEquals(testUser.getUser_id(), createdTask.getUser().getUser_id());
    }

    @Test
    public void testGetTasksForUser() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(testTask);

        when(taskRepository.findByUser(testUser)).thenReturn(tasks);

        ResponseEntity<List<Task>> responseEntity = taskService.getTasksForUser(new UserPrincipal(testUser));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }
}