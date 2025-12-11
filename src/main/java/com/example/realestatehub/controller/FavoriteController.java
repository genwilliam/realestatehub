package com.example.realestatehub.controller;

import com.example.realestatehub.common.ApiResponse;
import com.example.realestatehub.service.FavoriteService;
import com.example.realestatehub.util.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:63344") // 允许跨域
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{houseId}")
    public ApiResponse<Void> add(@PathVariable @NotNull Long houseId) {
        favoriteService.addFavorite(SecurityUtils.currentUserId(), houseId);
        return ApiResponse.ok("收藏成功", null);
    }

    @DeleteMapping("/{houseId}")
    public ApiResponse<Void> remove(@PathVariable @NotNull Long houseId) {
        favoriteService.removeFavorite(SecurityUtils.currentUserId(), houseId);
        return ApiResponse.ok("取消收藏成功", null);
    }
}

