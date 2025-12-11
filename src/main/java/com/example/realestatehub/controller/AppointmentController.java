package com.example.realestatehub.controller;

import com.example.realestatehub.common.ApiResponse;
import com.example.realestatehub.dto.appointment.AppointmentCreateRequest;
import com.example.realestatehub.dto.appointment.AppointmentUpdateStatusRequest;
import com.example.realestatehub.model.entity.Appointment;
import com.example.realestatehub.service.AppointmentService;
import com.example.realestatehub.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:63344") // 允许跨域
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<Appointment> create(@Valid @RequestBody AppointmentCreateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        return ApiResponse.ok(appointmentService.create(userId, request));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Appointment> updateStatus(@PathVariable Long id,
                                                 @Valid @RequestBody AppointmentUpdateStatusRequest request) {
        Long userId = SecurityUtils.currentUserId();
        return ApiResponse.ok(appointmentService.updateStatus(id, request, userId));
    }

    @GetMapping("/me")
    public ApiResponse<List<Appointment>> myAppointments() {
        Long userId = SecurityUtils.currentUserId();
        return ApiResponse.ok(appointmentService.listByUser(userId));
    }
}

