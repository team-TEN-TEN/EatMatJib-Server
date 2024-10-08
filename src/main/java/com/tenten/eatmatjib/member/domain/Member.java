package com.tenten.eatmatjib.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String account;

    @Column(nullable = false)
    private String password;

    private BigDecimal x;

    private BigDecimal y;

    @Column(nullable = false)
    private Boolean isRecommendationActive;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Builder
    public Member(
            String account,
            String password,
            BigDecimal x,
            BigDecimal y,
            Boolean isRecommendationActive,
            LocalDateTime joinedAt
    ) {
        this.account = account;
        this.password = password;
        this.x = x;
        this.y = y;
        this.isRecommendationActive =
                (isRecommendationActive != null) ? isRecommendationActive : true;
        this.joinedAt = joinedAt;
    }

    public void updateInfo(Coord coord, Boolean isRecommendationActive) {
        this.x = BigDecimal.valueOf(coord.x());
        this.y = BigDecimal.valueOf(coord.y());
        this.isRecommendationActive = isRecommendationActive;
    }
}
