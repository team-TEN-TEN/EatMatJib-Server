package com.tenten.eatmatjib.region.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("시-도")
    private String city;

    @Column(nullable = false)
    @Comment("시군구")
    private String district;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void update(String city, String district) {
        this.city = city;
        this.district = district;
    }

    @Builder
    public Region(String city, String district) {
        this.city = city;
        this.district = district;
        this.updatedAt = LocalDateTime.now();
    }
}
