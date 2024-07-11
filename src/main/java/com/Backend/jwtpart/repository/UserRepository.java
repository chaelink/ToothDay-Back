package com.Backend.jwtpart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Backend.jwtpart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}