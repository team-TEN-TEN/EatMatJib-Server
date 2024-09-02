package com.tenten.eatmatjib.review.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.review.dto.ReviewRequest;
import com.tenten.eatmatjib.review.service.AddReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class ReviewControllerTest {

  private MockMvc mockMvc;

  @Mock
  private AddReviewService addReviewService;

  @InjectMocks
  private ReviewController reviewController;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter()) // Jackson converter 추가
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .build();
  }

  @DisplayName("정상 케이스 테스트 (201) - 서비스 의존성 제거")
  @Test
  void testAddReview_whenMemberAndRestaurantExist_thenReturnCreated() throws Exception {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount("ten")
        .restaurantId(1L)
        .content("정말 맛있어요!")
        .score(5)
        .build();

    doNothing().when(addReviewService).addReviewAndUpdateRating(any(ReviewRequest.class));

    // When & Then
    mockMvc.perform(post("/api/v1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(new ObjectMapper().writeValueAsString(reviewRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string("\"리뷰가 생성되었습니다.\""));

    verify(addReviewService).addReviewAndUpdateRating(any(ReviewRequest.class));
  }

  @DisplayName("memberAccount가 존재하지 않는 경우 404에러")
  @Test
  void testAddReview_whenMemberNotExists_thenReturnNotFound() throws Exception {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount("ten")
        .restaurantId(1L)
        .content("정말 맛있어요!")
        .score(5)
        .build();

    doThrow(new BusinessException(ErrorCode.MEMBER_NOT_FOUND))
        .when(addReviewService).addReviewAndUpdateRating(reviewRequest);

    // When & Then
    mockMvc.perform(post("/api/v1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reviewRequest)))
        .andExpect(status().isNotFound());
  }

  @DisplayName("restaurantId가 존재하지 않는 경우 404에러")
  @Test
  void testAddReview_whenRestaurantNotExists_thenReturnNotFound() throws Exception {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount("ten")
        .restaurantId(1L)
        .content("정말 맛있어요!")
        .score(5)
        .build();

    doThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND))
        .when(addReviewService).addReviewAndUpdateRating(reviewRequest);

    // When & Then
    mockMvc.perform(post("/api/v1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reviewRequest)))
        .andExpect(status().isNotFound());
  }


}
