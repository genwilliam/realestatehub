package com.example.realestatehub.dto.house;

import com.example.realestatehub.model.enums.HouseStatus;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Data
public class HouseQuery {
    private String region;
    private HouseStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String layout;
    private int page = 0;
    private int size = 10;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sortBy = "createdAt";
}

