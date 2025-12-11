package com.example.realestatehub.service.impl;

import com.example.realestatehub.common.exception.BusinessException;
import com.example.realestatehub.model.entity.Favorite;
import com.example.realestatehub.model.entity.House;
import com.example.realestatehub.model.entity.User;
import com.example.realestatehub.repository.FavoriteRepository;
import com.example.realestatehub.repository.HouseRepository;
import com.example.realestatehub.repository.UserRepository;
import com.example.realestatehub.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Override
    @Transactional
    public void addFavorite(Long userId, Long houseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        House house = houseRepository.findById(houseId).orElseThrow(() -> new BusinessException("房源不存在"));
        if (favoriteRepository.existsByUserAndHouse(user, house)) {
            return;
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setHouse(house);
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long houseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        House house = houseRepository.findById(houseId).orElseThrow(() -> new BusinessException("房源不存在"));
        favoriteRepository.findByUserAndHouse(user, house).ifPresent(favoriteRepository::delete);
    }
}

