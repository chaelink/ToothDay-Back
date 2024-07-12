package com.Backend.ToothDay.jwt.repository;

import com.Backend.ToothDay.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}