package com.tenten.eatmatjib.review.repository;

import com.tenten.eatmatjib.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
