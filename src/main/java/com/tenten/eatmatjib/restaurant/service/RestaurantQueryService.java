package com.tenten.eatmatjib.restaurant.service;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import com.tenten.eatmatjib.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import com.tenten.eatmatjib.common.util.CoordinateConverter;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantQueryService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CoordinateConverter coordinateConverter;

    public Restaurant getRestaurantDetail(Long restaurantId) {
        // Restaurant 엔티티를 ID로 조회
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));
    }

    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        // Review 엔티티를 Restaurant ID로 조회하고 최신순으로 정렬
        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);

    }
    public Page<RestaurantsQueryRes> execute(
            Long memberId,
            RestaurantSearchCondition searchCondition,
            Coord memberLocation,
            Pageable pageable
    ) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Coord convertedMemberLocation = coordinateConverter.convert(memberLocation);

        return restaurantRepository.search(searchCondition, convertedMemberLocation, pageable);
    }

}
