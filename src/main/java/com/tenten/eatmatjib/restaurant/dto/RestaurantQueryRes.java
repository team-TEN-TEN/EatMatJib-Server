package com.tenten.eatmatjib.restaurant.dto;


import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.review.domain.Review;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RestaurantQueryRes {

    private Long id;
    private String name;
    private String zipCode;
    private String address;
    private String cuisine;
    private BigDecimal x;
    private BigDecimal y;
    private String phoneNumber;
    private String homepageUrl;
    private BigDecimal avgScore;
    private int viewCount;
    private LocalDateTime updatedAt;
    private List<ReviewResponse> reviews;

    public RestaurantQueryRes(Restaurant restaurant, List<Review> reviews) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.zipCode = restaurant.getZipCode();
        this.address = restaurant.getAddress();
        this.cuisine = restaurant.getCuisine();
        this.x = restaurant.getX();
        this.y = restaurant.getY();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.homepageUrl = restaurant.getHomepageUrl();
        this.avgScore = restaurant.getAvgScore();
        this.viewCount = restaurant.getViewCount();
        this.updatedAt = restaurant.getUpdatedAt();
        this.reviews = reviews.stream().map(ReviewResponse::new).toList();
    }
}

@Getter
class ReviewResponse {

    private Long id;
    private String content;
    private int score;
    private LocalDateTime createdAt;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.score = review.getScore();
        this.createdAt = review.getCreatedAt();
    }
}
