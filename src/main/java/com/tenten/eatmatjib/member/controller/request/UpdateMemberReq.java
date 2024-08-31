package com.tenten.eatmatjib.member.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사용자 설정 업데이트 요청 객체")
@Getter
@Builder
public class UpdateMemberReq {

    @Schema(description = "위도", example = "37.5665")
    @NotNull(message = "위도는 필수 입력입니다.")
    @Min(value = -90, message = "위도는 -90 이상이어야 합니다.")
    @Max(value = 90, message = "위도는 90 이하여야 합니다.")
    private Double lat;

    @Schema(description = "경도", example = "126.9780")
    @NotNull(message = "경도는 필수 입력입니다.")
    @Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
    @Max(value = 180, message = "경도는 180 이하여야 합니다.")
    private Double lon;

    @Schema(description = "점심 추천 기능 사용 여부", example = "true")
    @NotNull(message = "점심 추천 기능 사용 여부는 필수 입력입니다.")
    private Boolean isRecommendationActive;
}
