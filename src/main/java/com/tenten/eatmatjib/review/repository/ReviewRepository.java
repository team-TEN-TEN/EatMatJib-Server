package com.tenten.eatmatjib.review.repository;

import com.tenten.eatmatjib.review.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  // Restaurant ID를 기준으로 Review 목록을 가져오되, 최신순으로 정렬
  List<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
}
