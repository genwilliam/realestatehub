package com.example.realestatehub.repository;

import com.example.realestatehub.model.entity.House;
import com.example.realestatehub.model.enums.HouseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface HouseRepository extends JpaRepository<House, Long> {

    @Query("""
            select h from House h
            where (:region is null or h.region = :region)
              and (:status is null or h.status = :status)
              and (:minPrice is null or h.price >= :minPrice)
              and (:maxPrice is null or h.price <= :maxPrice)
              and (:layout is null or h.layout = :layout)
            """)
    Page<House> search(@Param("region") String region,
                       @Param("status") HouseStatus status,
                       @Param("minPrice") BigDecimal minPrice,
                       @Param("maxPrice") BigDecimal maxPrice,
                       @Param("layout") String layout,
                       Pageable pageable);
}

