package com.tenten.eatmatjib.review.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

  @DisplayName("ReviewController 테스트. 서비스와 의존성 제거")
  @Test
  void testAddReview() throws Exception {
    // Given
    ReviewRequest reviewRequest = ReviewRequest.builder()
        .memberAccount("ten")
        .restaurantId(1L)
        .content("정말 맛있어요!")
        .score(5)
        .build();

    doNothing().when(addReviewService).addReviewAndUpdateRating(any(ReviewRequest.class));

    // When & Then
    mockMvc.perform(put("/api/v1/review")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(new ObjectMapper().writeValueAsString(reviewRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string("\"리뷰가 생성되었습니다.\""));

    verify(addReviewService).addReviewAndUpdateRating(any(ReviewRequest.class));
  }
}
