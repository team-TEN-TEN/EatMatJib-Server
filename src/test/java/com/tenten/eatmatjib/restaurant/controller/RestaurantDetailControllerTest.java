package com.tenten.eatmatjib.restaurant.controller;

import com.tenten.eatmatjib.common.config.auth.JwtUtil;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RestaurantQueryService restaurantQueryService;

    private Restaurant restaurant;
    private Member member;
    private List<Review> reviews;

    @BeforeEach
    void setUp() {
        // Given: 테스트 데이터 준비
        long restaurantId = 1L;
        restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("맛집")
                .zipCode("12345")
                .address("서울시 강남구")
                .cuisine("한식")
                .x(BigDecimal.valueOf(127))
                .y(BigDecimal.valueOf(37))
                .phoneNumber("010-1234-5678")
                .homepageUrl("http://맛집.com")
                .avgScore(BigDecimal.valueOf(4))
                .viewCount(100)
                .updatedAt(LocalDateTime.now())
                .build();

        // 멤버 엔티티 생성
        member = Member.builder()
                .account("John Doe")
                .password("password123")
                .joinedAt(LocalDateTime.now())
                .build();

        // 첫 번째 리뷰 엔티티 생성 (최근 리뷰)
        Review review1 = Review.builder()
                .content("정말 맛있어요!")
                .score(5)
                .createdAt(LocalDateTime.now()) // 최신 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        // 두 번째 리뷰 엔티티 생성 (오래된 리뷰)
        Review review2 = Review.builder()
                .content("그저 그랬어요.")
                .score(3)
                .createdAt(LocalDateTime.now().minusDays(1)) // 하루 전 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        reviews = List.of(review1, review2);
    }

    @DisplayName("정상 케이스 테스트. restaurantId가 존재하고, 리뷰는 최신 순으로 반환.")
    @Test
    void getRestaurantDetail_ReturnsRestaurantDetailsAndReviews() throws Exception {
        // Given: 테스트 데이터 준비
        long restaurantId = 1L;

        // When: Service 메서드가 호출될 때, 미리 정의한 객체를 반환하도록 설정
        when(restaurantQueryService.getRestaurantDetail(anyLong())).thenReturn(restaurant);
        when(restaurantQueryService.getReviewsByRestaurantId(anyLong())).thenReturn(reviews);

        // Then: 컨트롤러의 엔드포인트를 호출하고, 예상된 응답이 반환되는지 검증
        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/detail",
                        restaurantId)  // 테스트할 URL 및 PathVariable 설정
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurantId))  // JSON 응답의 특정 필드 검증
                .andExpect(jsonPath("$.name").value("맛집"))
                .andExpect(jsonPath("$.reviews", hasSize(2)))  // 리뷰 리스트 크기 검증
                .andExpect(jsonPath("$.reviews[0].content").value("정말 맛있어요!"))
                .andExpect(jsonPath("$.reviews[1].content").value("그저 그랬어요."));
    }

    @DisplayName("존재하지 않는 restaurandId로 음식점 상세정보를 조회하려 할 때 404를 반환한다.")
    @Test
    void getRestaurantDetail_whenRestaurantDoesNotExist_thenReturn404() throws Exception {
        // given
        Long restaurantId = 1L;
        when(restaurantQueryService.getRestaurantDetail(restaurantId)).thenThrow(
                new BusinessException(
                        ErrorCode.RESTAURANT_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/detail", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}