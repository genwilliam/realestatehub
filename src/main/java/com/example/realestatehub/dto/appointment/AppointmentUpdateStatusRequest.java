package com.example.realestatehub.dto.appointment;

import com.example.realestatehub.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentUpdateStatusRequest {
    @NotNull(message = "状态不能为空")
    private AppointmentStatus status;

    private String remark;
}

