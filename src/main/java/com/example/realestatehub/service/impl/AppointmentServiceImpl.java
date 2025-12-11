package com.example.realestatehub.service.impl;

import com.example.realestatehub.common.exception.BusinessException;
import com.example.realestatehub.dto.appointment.AppointmentCreateRequest;
import com.example.realestatehub.dto.appointment.AppointmentUpdateStatusRequest;
import com.example.realestatehub.model.entity.Agent;
import com.example.realestatehub.model.entity.Appointment;
import com.example.realestatehub.model.entity.House;
import com.example.realestatehub.model.entity.User;
import com.example.realestatehub.model.enums.Role;
import com.example.realestatehub.repository.AgentRepository;
import com.example.realestatehub.repository.AppointmentRepository;
import com.example.realestatehub.repository.HouseRepository;
import com.example.realestatehub.repository.UserRepository;
import com.example.realestatehub.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final AgentRepository agentRepository;

    @Override
    @Transactional
    public Appointment create(Long userId, AppointmentCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new BusinessException("房源不存在"));
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setHouse(house);
        appointment.setAgent(house.getAgent());
        appointment.setScheduledAt(request.getScheduledAt());
        appointment.setRemark(request.getRemark());
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateStatus(Long appointmentId, AppointmentUpdateStatusRequest request, Long operatorUserId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        User operator = userRepository.findById(operatorUserId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRole() == Role.AGENT) {
            Agent agent = agentRepository.findByUser(operator)
                    .orElseThrow(() -> new BusinessException("经纪人信息不存在"));
            if (appointment.getAgent() == null || !appointment.getAgent().getId().equals(agent.getId())) {
                throw new BusinessException("无权处理该预约");
            }
        }
        appointment.setStatus(request.getStatus());
        appointment.setRemark(request.getRemark());
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> listByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        return appointmentRepository.findByUser(user);
    }
}

