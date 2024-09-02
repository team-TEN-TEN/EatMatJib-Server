package com.tenten.eatmatjib.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewRequest {


    private String content; // 리뷰 내용
    private int score;      // 평점
    private String memberAccount;  // 리뷰를 작성한 멤버의 ID
    private Long restaurantId;

    @Builder
    public ReviewRequest(String content, int score, String memberAccount, Long restaurantId) {
        this.content = content;
        this.score = score;
        this.memberAccount = memberAccount;
        this.restaurantId = restaurantId;
    }
}
