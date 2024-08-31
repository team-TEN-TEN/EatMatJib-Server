package com.tenten.eatmatjib.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequest {


  private String content; // 리뷰 내용
  private int score;      // 평점
  private Long memberId;  // 리뷰를 작성한 멤버의 ID
  private Long restaurantId;
}
