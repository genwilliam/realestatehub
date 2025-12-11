package com.example.realestatehub.service;

import com.example.realestatehub.dto.appointment.AppointmentCreateRequest;
import com.example.realestatehub.dto.appointment.AppointmentUpdateStatusRequest;
import com.example.realestatehub.model.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment create(Long userId, AppointmentCreateRequest request);
    Appointment updateStatus(Long appointmentId, AppointmentUpdateStatusRequest request, Long operatorUserId);
    List<Appointment> listByUser(Long userId);
}

