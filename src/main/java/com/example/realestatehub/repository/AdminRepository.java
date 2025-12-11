package com.example.realestatehub.repository;

import com.example.realestatehub.model.entity.Admin;
import com.example.realestatehub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
}

