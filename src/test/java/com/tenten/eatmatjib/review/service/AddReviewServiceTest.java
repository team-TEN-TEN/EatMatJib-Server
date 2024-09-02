package com.tenten.eatmatjib.review.service;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.dto.ReviewRequest;
import com.tenten.eatmatjib.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class AddReviewServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private RestaurantRepository restaurantRepository;

  @InjectMocks
  private AddReviewService addReviewService;

  private Restaurant restaurant;
  private Member member;
  private Review review;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // 초기화된 Member와 Restaurant ID 설정
    member = Member.builder()
        .account("John Doe")
        .password("password123")
        .joinedAt(LocalDateTime.now())
        .build();

    restaurant = Restaurant.builder()
        .id(1L)
        .name("맛집")
        .zipCode("12345")
        .address("서울시 강남구")
        .cuisine("한식")
        .x(BigDecimal.valueOf(127.12345))
        .y(BigDecimal.valueOf(37.56789))
        .phoneNumber("010-1234-5678")
        .homepageUrl("http://맛집.com")
        .avgScore(BigDecimal.ZERO)
        .viewCount(100)
        .updatedAt(LocalDateTime.now())
        .build();

    review = Review.builder()
        .content("정말 맛있어요!")
        .score(5)
        .createdAt(LocalDateTime.now())
        .member(member)
        .restaurant(restaurant)
        .build();
  }

  @DisplayName("정상 케이스 (200). 두 리뷰 점수의 평균으로 음식점의 score값이 반영된 경우")
  @Test
  void testAddReviewAndUpdateRating() {


    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount(member.getAccount())
        .restaurantId(restaurant.getId())
        .content(review.getContent())
        .score(review.getScore())
        .build();

    when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
    when(memberRepository.findByAccount(anyString())).thenReturn(Optional.of(member));
    when(reviewRepository.findByRestaurantId(anyLong())).thenReturn(List.of(review));

    // When
    addReviewService.addReviewAndUpdateRating(reviewRequest);


    // Capture the argument passed to reviewRepository.save
    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
    verify(reviewRepository).save(reviewCaptor.capture());

    // Verify the captured review
    Review capturedReview = reviewCaptor.getValue();
    assertEquals(review.getContent(), capturedReview.getContent());
    assertEquals(review.getScore(), capturedReview.getScore());


    assertEquals(member, capturedReview.getMember());
    assertEquals(restaurant, capturedReview.getRestaurant());

    // Verify that the restaurant's average score was updated
    ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
    verify(restaurantRepository).save(restaurantCaptor.capture());

    // Verify the captured restaurant
    Restaurant capturedRestaurant = restaurantCaptor.getValue();
    assertEquals(restaurant.getId(), capturedRestaurant.getId());

    // Since average score calculation should be based on the reviews, verify the calculated average score
    BigDecimal expectedAvgScore = BigDecimal.valueOf(5.0); // Calculate expected average score based on test data
    assertEquals(expectedAvgScore, capturedRestaurant.getAvgScore());
  }

  @DisplayName("restauranId에 해당하는 음식점이 없는 경우")
  @Test
  void testAddReviewAndUpdateRating_RestaurantNotFound() {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount(member.getAccount())
        .restaurantId(restaurant.getId())
        .content(review.getContent())
        .score(review.getScore())
        .build();

    when(restaurantRepository.findById(reviewRequest.getRestaurantId()))
        .thenReturn(Optional.empty());

    // When & Then
    BusinessException thrown = assertThrows(BusinessException.class, () ->
        addReviewService.addReviewAndUpdateRating(reviewRequest));
    assertEquals(ErrorCode.RESTAURANT_NOT_FOUND, thrown.getErrorCode());
  }

  @DisplayName("memberAccount에 해당하는 멤버가 존재하지 않는 경우")
  @Test
  void testAddReviewAndUpdateRating_MemberNotFound() {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount(member.getAccount())
        .restaurantId(restaurant.getId())
        .content(review.getContent())
        .score(review.getScore())
        .build();

    when(restaurantRepository.findById(reviewRequest.getRestaurantId()))
        .thenReturn(Optional.of(restaurant));
    when(memberRepository.findByAccount(reviewRequest.getMemberAccount()))
        .thenReturn(Optional.empty());

    // When & Then
    BusinessException thrown = assertThrows(BusinessException.class, () ->
        addReviewService.addReviewAndUpdateRating(reviewRequest));
    assertEquals(ErrorCode.MEMBER_NOT_FOUND, thrown.getErrorCode());
  }
}
