package com.example.realestatehub.repository;

import com.example.realestatehub.model.entity.Appointment;
import com.example.realestatehub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser(User user);
}

