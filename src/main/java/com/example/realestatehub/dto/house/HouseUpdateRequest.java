package com.example.realestatehub.dto.house;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HouseUpdateRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Double area;
    private String region;
    private String address;
    private String layout;
    private List<String> tags;
    private List<String> images;
}

