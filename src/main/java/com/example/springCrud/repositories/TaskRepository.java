package com.example.springCrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springCrud.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
