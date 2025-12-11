package com.example.realestatehub.repository;

import com.example.realestatehub.model.entity.Agent;
import com.example.realestatehub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByUser(User user);
}

