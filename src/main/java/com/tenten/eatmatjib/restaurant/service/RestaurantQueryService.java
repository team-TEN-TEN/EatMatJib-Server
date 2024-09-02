package com.tenten.eatmatjib.restaurant.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.util.CoordinateConverter;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantQueryService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final CoordinateConverter coordinateConverter;

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
