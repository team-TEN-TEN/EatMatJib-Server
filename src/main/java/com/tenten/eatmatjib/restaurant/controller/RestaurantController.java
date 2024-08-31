package com.tenten.eatmatjib.restaurant.controller;


import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.dto.RestaurantQueryRes;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "음식점")
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantQueryService restaurantQueryService;

  @GetMapping("/{id}/detail")
  @Operation(
      summary = "음식점 상세조회",
      description = "요청받은 restaurantId의 음식점에 대해 상세정보(모든필드)를 반환합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "음식점의 모든 필드를 포함하는 DTO 반환"),
          @ApiResponse(responseCode = "404", description = "음식점이 존재하지 않습니다."),
      }
  )
  public ResponseEntity<RestaurantQueryRes> getRestaurantDetail(@PathVariable Long id) {
    Restaurant restaurant = restaurantQueryService.getRestaurantDetail(id);
    List<Review> reviews = restaurantQueryService.getReviewsByRestaurantId(id);
    RestaurantQueryRes response = new RestaurantQueryRes(restaurant, reviews);
    return ResponseEntity.ok(response);
  }
}
