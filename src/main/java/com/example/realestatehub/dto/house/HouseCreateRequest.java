package com.example.realestatehub.dto.house;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HouseCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "面积不能为空")
    private Double area;

    private String region;
    private String address;
    private String layout;
    private List<String> tags;
    private List<String> images;
}

