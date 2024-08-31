package com.tenten.eatmatjib.restaurant.domain;

import com.tenten.eatmatjib.review.domain.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
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

    // Review와의 일대다 관계 설정
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @Builder // 빌더 패턴을 위한 생성자
    public Restaurant(Long id, String name, String zipCode, String address, String cuisine,
        BigDecimal x, BigDecimal y, String phoneNumber, String homepageUrl,
        BigDecimal avgScore, int viewCount, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.cuisine = cuisine;
        this.x = x;
        this.y = y;
        this.phoneNumber = phoneNumber;
        this.homepageUrl = homepageUrl;
        this.avgScore = avgScore;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }
}
