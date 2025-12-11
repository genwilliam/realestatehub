package com.example.realestatehub.model.entity;

import com.example.realestatehub.model.enums.HouseStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "house")
public class House extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 2000)
    private String description;

    private BigDecimal price;

    private Double area;

    @Column(length = 50)
    private String region;

    @Column(length = 100)
    private String address;

    @Column(length = 50)
    private String layout; // 户型

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HouseStatus status = HouseStatus.DRAFT;

    private boolean approved = false;

    private LocalDateTime publishTime;

    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseTag> tags = new ArrayList<>();
}

