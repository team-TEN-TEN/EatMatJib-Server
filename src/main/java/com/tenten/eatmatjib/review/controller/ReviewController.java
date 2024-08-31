package com.tenten.eatmatjib.review.controller;

import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.dto.ReviewRequest;
import com.tenten.eatmatjib.review.service.AddReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

  AddReviewService addReviewService;


  @PutMapping
  @Operation(
      summary = "리뷰 생성",
      description = "member 테이블에 memberId, restaurant 테이블에 restaurantId와 매핑된 리뷰를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "201", description = "리뷰가 생성되었습니다."),
          @ApiResponse(responseCode = "404", description = "멤버 혹은 음식점이 존재하지 않습니다.")
      }
  )
  public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest) {
    addReviewService.addReviewAndUpdateRating(reviewRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body("리뷰가 생성되었습니다.");
  }

}
