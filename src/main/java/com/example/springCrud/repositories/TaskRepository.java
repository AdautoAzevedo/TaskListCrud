package com.example.springCrud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springCrud.model.Task;
import com.example.springCrud.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
