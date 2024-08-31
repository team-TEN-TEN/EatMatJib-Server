package com.tenten.eatmatjib.member.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tenten.eatmatjib.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사용자 정보 응답 객체")
@Getter
@Builder
public class InfoMemberRes {

    @Schema(description = "사용자 id", example = "1")
    private Long id;

    @Schema(description = "사용자 계정", example = "tenten")
    private String account;

    @Schema(description = "사용자 x좌표", example = "200000.1234567891")
    private BigDecimal x;

    @Schema(description = "사용자 y좌표", example = "200000.1234567891")
    private BigDecimal y;

    @Schema(description = "사용자 점심 추천 기능 사용 여부", example = "true")
    private Boolean isRecommendationActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "사용자 가입 날짜", example = "2024-08-31")
    private LocalDate joinedAt;

    public static InfoMemberRes of(Member member) {
        return InfoMemberRes.builder()
            .id(member.getId())
            .account(member.getAccount())
            .x(member.getX())
            .y(member.getY())
            .isRecommendationActive(member.getIsRecommendationActive())
            .joinedAt(LocalDate.from(member.getJoinedAt()))
            .build();
    }
}
