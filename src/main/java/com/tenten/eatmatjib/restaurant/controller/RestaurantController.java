package com.tenten.eatmatjib.restaurant.controller;

import com.tenten.eatmatjib.common.exception.ErrorResponse;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.dto.RestaurantQueryRes;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tenten.eatmatjib.common.config.auth.AuthUser;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurant", description = "맛집 API")
public class RestaurantController {

    private final RestaurantQueryService restaurantQueryService;


    @GetMapping
    @Operation(summary = "맛집 목록")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "맛집 목록 조회 성공")})
    public ResponseEntity<Page<RestaurantsQueryRes>> lists(
        @Parameter(hidden = true) @AuthUser Long memberId,
        @Parameter(description = "위도", example = "37.5665")
        @RequestParam double lat,
        @Parameter(description = "경도", example = "126.9780")
        @RequestParam double lon,
        @Parameter(description = "범위(km)", example = "1")
        @RequestParam int range,
        @Parameter(description = "검색어", example = "치카바")
        @RequestParam(required = false) String keyword,
        @Parameter(description = "필터링 기준", example = "일식")
        @RequestParam(required = false) String filterBy,
        @Parameter(description = "정렬 기준")
        @RequestParam(required = false, defaultValue = "distance,rate") String orderBy,
        @Parameter(description = "페이지 번호")
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @Parameter(description = "페이지 크기")
        @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        RestaurantSearchCondition searchCondition =
            RestaurantSearchCondition.of(range, keyword, filterBy, orderBy);
        Coord memberLocation = Coord.of(lat, lon);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<RestaurantsQueryRes> response =
            restaurantQueryService.execute(memberId, searchCondition, memberLocation, pageRequest);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{restaurantId}/detail")
    @Operation(
            summary = "음식점 상세조회",
            description = "요청받은 restaurantId의 음식점에 대해 상세정보(모든필드)를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "음식점의 모든 필드를 포함하는 DTO 반환"),
                    @ApiResponse(responseCode = "404", content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))}),
            }
    )
    public ResponseEntity<RestaurantQueryRes> getRestaurantDetail(
            @PathVariable("restaurantId") Long id) {
        Restaurant restaurant = restaurantQueryService.getRestaurantDetail(id);
        List<Review> reviews = restaurantQueryService.getReviewsByRestaurantId(id);
        RestaurantQueryRes response = new RestaurantQueryRes(restaurant, reviews);
        return ResponseEntity.ok(response);
    }
}
