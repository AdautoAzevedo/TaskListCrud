package com.example.springCrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springCrud.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    UserDetails findByLogin(String login);   
}
