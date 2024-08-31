package com.tenten.eatmatjib.restaurant.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.common.exception.GlobalExceptionHandler;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import com.tenten.eatmatjib.review.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

  @Mock
  private RestaurantQueryService restaurantQueryService;

  @InjectMocks
  private RestaurantController restaurantController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = standaloneSetup(restaurantController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .alwaysDo(print())
        .build();
  }

  @DisplayName("restaurantId가 존재하는 경우 200과 함께 음식점 상세 정보를 반환한다.")
  @Test
  void getRestaurantDetail_whenRestaurantExists_thenReturnRestaurantWithReviews() throws Exception {
    // given
    Long restaurantId = 1L;
    Restaurant restaurant = Restaurant.builder()
        .id(restaurantId)
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

    Review review1 = Review.builder()
        .id(1L)
        .content("Nice ambiance.")
        .score(5)
        .createdAt(LocalDateTime.now().minusDays(1))
        .build();
    Review review2 = Review.builder()
        .id(2L)
        .content("Great food!")
        .score(4)
        .createdAt(LocalDateTime.now())
        .build();

    when(restaurantQueryService.getRestaurantDetail(restaurantId)).thenReturn(restaurant);
    when(restaurantQueryService.getReviewsByRestaurantId(restaurantId)).thenReturn(List.of(review2, review1));

    // when & then
    mockMvc.perform(get("/api/v1/restaurants/{id}/detail", restaurantId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(restaurantId))
        .andExpect(jsonPath("$.name").value("Test Restaurant"))
        .andExpect(jsonPath("$.reviews").isArray())
        .andExpect(jsonPath("$.reviews[0].content").value("Great food!"))
        .andExpect(jsonPath("$.reviews[1].content").value("Nice ambiance."));
  }

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
