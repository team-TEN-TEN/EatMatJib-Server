package com.tenten.eatmatjib.restaurant.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.repository.ReviewRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class RestaurantQueryServiceTest {

  @Mock
  private RestaurantRepository restaurantRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @InjectMocks
  private RestaurantQueryService restaurantQueryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getRestaurantDetail_whenRestaurantExists_thenReturnRestaurant() {
    // given
    Long restaurantId = 1L;
    Restaurant restaurant = Restaurant.builder()
        .name("Test Restaurant")
        .address("123 Test Street")
        .zipCode("12345")
        .cuisine("Korean")
        .x(BigDecimal.valueOf(37.5665))
        .y(BigDecimal.valueOf(126.9780))
        .phoneNumber("123-456-7890")
        .homepageUrl("http://example.com")
        .avgScore(BigDecimal.valueOf(4.5))
        .viewCount(100)
        .updatedAt(LocalDateTime.now())
        .build();

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

    // when
    Restaurant foundRestaurant = restaurantQueryService.getRestaurantDetail(restaurantId);

    // then
    assertThat(foundRestaurant).isNotNull();
    assertThat(foundRestaurant.getName()).isEqualTo("Test Restaurant");
  }

  @Test
  void getRestaurantDetail_whenRestaurantDoesNotExist_thenThrowException() {
    // given
    Long restaurantId = 1L;
    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> restaurantQueryService.getRestaurantDetail(restaurantId));
  }

  @Test
  void getReviewsByRestaurantId_whenReviewsExist_thenReturnReviewsSortedByCreatedAtDesc() {
    // given
    Long restaurantId = 1L;
    Review review1 = Review.builder()
        .content("Review 1")
        .score(5)
        .createdAt(LocalDateTime.now().minusDays(1))
        .build();
    Review review2 = Review.builder()
        .content("Review 2")
        .score(4)
        .createdAt(LocalDateTime.now())
        .build();
    when(reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId)).thenReturn(
        List.of(review2, review1));

    // when
    List<Review> reviews = restaurantQueryService.getReviewsByRestaurantId(restaurantId);

    // then
    assertThat(reviews).hasSize(2);
    assertThat(reviews.get(0).getCreatedAt()).isAfter(reviews.get(1).getCreatedAt());
  }
}