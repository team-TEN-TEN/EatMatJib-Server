package com.tenten.eatmatjib.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantQueryServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantQueryService restaurantQueryService;

    private Restaurant restaurant;
    private Member member;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 음식점 엔티티 생성
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
                .avgScore(BigDecimal.valueOf(4.5))
                .viewCount(100)
                .updatedAt(LocalDateTime.now())
                .build();

        // 회원 엔티티 생성
        member = Member.builder()
                .account("John Doe")
                .password("password123")
                .joinedAt(LocalDateTime.now())
                .build();

        // 첫 번째 리뷰 엔티티 생성 (최근 리뷰)
        review1 = Review.builder()
                .content("정말 맛있어요!")
                .score(5)
                .createdAt(LocalDateTime.now()) // 최신 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        // 두 번째 리뷰 엔티티 생성 (오래된 리뷰)
        review2 = Review.builder()
                .content("그저 그랬어요.")
                .score(3)
                .createdAt(LocalDateTime.now().minusDays(1)) // 하루 전 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        restaurant.addReview(review1);
        restaurant.addReview(review2);


    }

    @DisplayName("정상 케이스 (200). 리뷰가 최신 순으로 정렬되었는지 확인.")
    @Test
    void getRestaurantDetail_whenRestaurantExists_thenReturnRestaurantWithReviewsInDescendingOrder() {
        // given
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // when
        Restaurant result = restaurantQueryService.getRestaurantDetail(restaurantId);

        // then
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        assertFalse(result.getReviews().isEmpty());
        assertEquals(2, result.getReviews().size());

        // 리뷰가 최신순으로 정렬되었는지 검증
        assertEquals(review1.getCreatedAt(), result.getReviews().get(0).getCreatedAt());
        assertEquals(review2.getCreatedAt(), result.getReviews().get(1).getCreatedAt());
        assertTrue(result.getReviews().get(0).getCreatedAt()
                .isAfter(result.getReviews().get(1).getCreatedAt()));
    }

    @DisplayName("restaurantId가 존재하지 않는 경우 404 에러")
    @Test
    void getRestaurantDetail_whenRestaurantDoesNotExist_thenThrowException() {
        // given
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> restaurantQueryService.getRestaurantDetail(restaurantId));

        // 예외에 올바른 오류 코드가 포함되어 있는지 확인
        assertEquals(ErrorCode.RESTAURANT_NOT_FOUND, exception.getErrorCode());
    }
}