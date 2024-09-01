package com.tenten.eatmatjib.restaurant.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "맛집 목록 응답 객체")
@Getter
@Builder
public class RestaurantsQueryRes {

    @Schema(description = "사업장명 id", example = "1")
    private Long id;

    @Schema(description = "사업장명", example = "치카바")
    private String name;

    @Schema(description = "주소", example = "서울특별시 송파구 송이로20길 12-1, 1층 101호 (가락동)")
    private String address;

    @Schema(description = "우편번호", example = "05712")
    private String zipCode;

    @Schema(description = "업태구분명", example = "일식")
    private String cuisine;

    @Schema(description = "전화번호", example = "02 448 6648")
    private String phoneNumber;

    @Schema(description = "홈페이지 주소", example = "https://chikaba.com")
    private String homepageUrl;

    @Schema(description = "평점", example = "4.5")
    private BigDecimal avgScore;

    @QueryProjection
    public RestaurantsQueryRes(
        Long id,
        String name,
        String address,
        String zipCode,
        String cuisine,
        String phoneNumber,
        String homepageUrl,
        BigDecimal avgScore
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.cuisine = cuisine;
        this.phoneNumber = phoneNumber;
        this.homepageUrl = homepageUrl;
        this.avgScore = avgScore;
    }
}
