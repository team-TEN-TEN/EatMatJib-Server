package com.tenten.eatmatjib.restaurant.service;


import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

  private final RestaurantRepository restaurantRepository;
  private final ReviewRepository reviewRepository;

  public Restaurant getRestaurantDetail(Long restaurantId) {
    // Restaurant 엔티티를 ID로 조회
    return restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));
  }

  public List<Review> getReviewsByRestaurantId(Long restaurantId) {
    // Review 엔티티를 Restaurant ID로 조회하고 최신순으로 정렬
    return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
  }
}
