package com.tenten.eatmatjib.review.controller;

import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.dto.ReviewRequest;
import com.tenten.eatmatjib.review.service.AddReviewService;
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

  @PutMapping()
  public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest) {
    addReviewService.addReviewAndUpdateRating(reviewRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body("리뷰가 생성되었습니다");
  }

}
