package com.example.realestatehub.service;

import com.example.realestatehub.dto.house.HouseCreateRequest;
import com.example.realestatehub.dto.house.HouseQuery;
import com.example.realestatehub.dto.house.HouseResponse;
import com.example.realestatehub.dto.house.HouseUpdateRequest;
import org.springframework.data.domain.Page;

public interface HouseService {
    HouseResponse create(HouseCreateRequest request, Long agentUserId);
    HouseResponse update(Long id, HouseUpdateRequest request, Long agentUserId);
    void delete(Long id, Long agentUserId);
    HouseResponse findById(Long id);
    Page<HouseResponse> search(HouseQuery query);
}

