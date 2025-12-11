package com.example.realestatehub.service;

public interface FavoriteService {
    void addFavorite(Long userId, Long houseId);
    void removeFavorite(Long userId, Long houseId);
}

