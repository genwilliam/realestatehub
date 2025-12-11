package com.example.realestatehub.repository;

import com.example.realestatehub.model.entity.Favorite;
import com.example.realestatehub.model.entity.House;
import com.example.realestatehub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndHouse(User user, House house);
    Optional<Favorite> findByUserAndHouse(User user, House house);
}

