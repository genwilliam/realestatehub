package com.example.realestatehub.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentCreateRequest {
    @NotNull(message = "房源ID不能为空")
    private Long houseId;

    @NotNull(message = "预约时间不能为空")
    @Future(message = "预约时间必须是未来时间")
    private LocalDateTime scheduledAt;

    private String remark;
}

