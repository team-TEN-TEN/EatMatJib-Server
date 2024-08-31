package com.tenten.eatmatjib.restaurant.controller;


import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.dto.RestaurantQueryRes;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantQueryService restaurantQueryService;

  @GetMapping("/{id}/detail")
  public ResponseEntity<RestaurantQueryRes> getRestaurantDetail(@PathVariable Long id) {
    Restaurant restaurant = restaurantQueryService.getRestaurantDetail(id);
    List<Review> reviews = restaurantQueryService.getReviewsByRestaurantId(id);
    RestaurantQueryRes response = new RestaurantQueryRes(restaurant, reviews);
    return ResponseEntity.ok(response);
  }
}
