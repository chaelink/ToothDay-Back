package com.Backend.ToothDay.jwt.repository;

import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.visit.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);

}