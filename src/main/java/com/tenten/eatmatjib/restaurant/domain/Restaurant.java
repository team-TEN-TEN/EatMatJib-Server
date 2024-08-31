package com.tenten.eatmatjib.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 50)
    private String cuisine;

    @Column(nullable = false)
    private BigDecimal x;

    @Column(nullable = false)
    private BigDecimal y;

    @Column(length = 20)
    private String phoneNumber;

    private String homepageUrl;

    @Column(nullable = false)
    private BigDecimal avgScore;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void updateAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }
}
