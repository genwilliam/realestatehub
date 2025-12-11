package com.example.realestatehub.dto.house;

import com.example.realestatehub.model.enums.HouseStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HouseResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Double area;
    private String region;
    private String address;
    private String layout;
    private HouseStatus status;
    private boolean approved;
    private LocalDateTime publishTime;
    private Long viewCount;
    private List<String> images;
    private List<String> tags;
    private String agentName;
    private String agentPhone;
}

