package com.tenten.eatmatjib.member.controller.response;

import com.tenten.eatmatjib.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사용자 설정 업데이트 응답 객체")
@Getter
@Builder
public class UpdateMemberRes {

    @Schema(description = "사용자 x좌표", example = "200000.1234567891")
    private BigDecimal x;

    @Schema(description = "사용자 y좌표", example = "200000.1234567891")
    private BigDecimal y;

    @Schema(description = "사용자 점심 추천 기능 사용 여부", example = "true")
    private Boolean isRecommendationActive;

    public static UpdateMemberRes of(Member member) {
        return UpdateMemberRes.builder()
            .x(member.getX())
            .y(member.getY())
            .isRecommendationActive(member.getIsRecommendationActive())
            .build();
    }
}
