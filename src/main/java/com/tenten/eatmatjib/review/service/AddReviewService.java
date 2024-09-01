package com.tenten.eatmatjib.review.service;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.dto.ReviewRequest;
import com.tenten.eatmatjib.review.repository.ReviewRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddReviewService {

  MemberRepository memberRepository;
  ReviewRepository reviewRepository;
  RestaurantRepository restaurantRepository;

  public void addReviewAndUpdateRating(ReviewRequest reviewRequest) {
    // 음식점 조회
    Restaurant restaurant = restaurantRepository.findById(reviewRequest.getRestaurantId())
        .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUNT));

    // 멤버 조회
    Member member = memberRepository.findByAccount(reviewRequest.getMemberAccount())
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

    // 리뷰 생성
    Review review = Review.builder()
        .content(reviewRequest.getContent())
        .score(reviewRequest.getScore())
        .createdAt(LocalDateTime.now())
        .member(member)
        .restaurant(restaurant)
        .build();


    reviewRepository.save(review);

    // 음식점의 평균 평점 업데이트
    List<Review> reviews = reviewRepository.findByRestaurantId(reviewRequest.getRestaurantId());
    BigDecimal avgScore = reviews.stream()
        .map(Review::getScore)
        .map(BigDecimal::valueOf)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .divide(BigDecimal.valueOf(reviews.size()), 1, BigDecimal.ROUND_HALF_UP);

    restaurant.addReview(review);
    restaurant.updateAvgScore(avgScore);
    restaurantRepository.save(restaurant);

  }


}

