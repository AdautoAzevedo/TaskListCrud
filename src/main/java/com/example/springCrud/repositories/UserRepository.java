package com.example.springCrud.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.springCrud.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);   
}
