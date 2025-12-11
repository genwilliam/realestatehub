package com.example.realestatehub.controller;

import com.example.realestatehub.common.ApiResponse;
import com.example.realestatehub.dto.house.HouseCreateRequest;
import com.example.realestatehub.dto.house.HouseQuery;
import com.example.realestatehub.dto.house.HouseResponse;
import com.example.realestatehub.dto.house.HouseUpdateRequest;
import com.example.realestatehub.service.HouseService;
import com.example.realestatehub.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "http://localhost:63344") // 允许跨域
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping
    public ApiResponse<Page<HouseResponse>> search(@Valid HouseQuery query) {
        return ApiResponse.ok(houseService.search(query));
    }

    @GetMapping("/{id}")
    public ApiResponse<HouseResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(houseService.findById(id));
    }

    @PostMapping
    public ApiResponse<HouseResponse> create(@Valid @RequestBody HouseCreateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        return ApiResponse.ok(houseService.create(request, userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<HouseResponse> update(@PathVariable Long id, @Valid @RequestBody HouseUpdateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        return ApiResponse.ok(houseService.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = SecurityUtils.currentUserId();
        houseService.delete(id, userId);
        return ApiResponse.ok("删除成功", null);
    }
}

