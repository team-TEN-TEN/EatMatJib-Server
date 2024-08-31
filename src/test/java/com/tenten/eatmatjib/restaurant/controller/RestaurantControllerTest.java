package com.tenten.eatmatjib.restaurant.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.common.exception.GlobalExceptionHandler;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.dto.RestaurantQueryRes;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

  @Mock
  private RestaurantQueryService restaurantQueryService;

  @InjectMocks
  private RestaurantController restaurantController;

  @Autowired
  RestaurantRepository restaurantRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


//  @DisplayName("restaurantId가 존재하는 경우 200과 함께 음식점 상세 정보를 반환한다.")
//  @Test
//  void getRestaurantDetail_whenRestaurantExists_thenReturnRestaurantWithReviews() throws Exception {
//    // 음식점 엔티티 생성
//    long restaurantId = 1L;
//    Restaurant restaurant = Restaurant.builder()
//        .name("맛집")
//        .zipCode("12345")
//        .address("서울시 강남구")
//        .cuisine("한식")
//        .x(BigDecimal.valueOf(127))
//        .y(BigDecimal.valueOf(37))
//        .phoneNumber("010-1234-5678")
//        .homepageUrl("http://맛집.com")
//        .avgScore(BigDecimal.valueOf(4))
//        .viewCount(100)
//        .updatedAt(LocalDateTime.now())
//        .build();
//
//    // 회원 엔티티 생성
//    Member member = Member.builder()
//        .account("John Doe")
//        .password("password123")
//        .joinedAt(LocalDateTime.now())
//        .build();
//
//    // 첫 번째 리뷰 엔티티 생성 (최근 리뷰)
//    Review review1 = Review.builder()
//        .content("정말 맛있어요!")
//        .score(5)
//        .createdAt(LocalDateTime.now()) // 최신 시간
//        .member(member)
//        .restaurant(restaurant)
//        .build();
//
//    // 두 번째 리뷰 엔티티 생성 (오래된 리뷰)
//    Review review2 = Review.builder()
//        .content("그저 그랬어요.")
//        .score(3)
//        .createdAt(LocalDateTime.now().minusDays(1)) // 하루 전 시간
//        .member(member)
//        .restaurant(restaurant)
//        .build();
//    restaurantRepository.save(restaurant);
//    reviewRepository.save(review1);
//    reviewRepository.save(review2);
//
//
//    // 음식점에 리뷰 추가 (리뷰가 최신순으로 정렬된 상태여야 함)
//    restaurant.addReview(review1);
//    restaurant.addReview(review2);
//
//    List<Review> reviews = List.of(review1, review2);
//
//
//
//    Restaurant res = restaurantQueryService.getRestaurantDetail(restaurantId);
//    List<Review> rev = restaurantQueryService.getReviewsByRestaurantId(restaurantId);
//    RestaurantQueryRes response = new RestaurantQueryRes(res, rev);
//
//
//    // Perform the request and verify the response
//    mockMvc.perform(get("/api/v1/restaurants/{id}/detail", restaurantId)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        .andExpect(content().json(objectMapper.writeValueAsString(response)));
//
//  }
  @DisplayName("존재하지 않는 restaurandId로 음식점 상세정보를 조회하려 할 때 404를 반환한다.")
  @Test
  void getRestaurantDetail_whenRestaurantDoesNotExist_thenReturn404() throws Exception {
    // given
    Long restaurantId = 1L;
    when(restaurantQueryService.getRestaurantDetail(restaurantId)).thenThrow(new BusinessException(
        ErrorCode.RESTAURANT_NOT_FOUNT));

    // when & then
    mockMvc.perform(get("/api/v1/restaurants/{id}/detail", restaurantId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
