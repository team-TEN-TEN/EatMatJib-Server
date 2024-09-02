package com.tenten.eatmatjib.restaurant.repository.custom;

import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantCustomRepository {

    Page<RestaurantsQueryRes> search(
        RestaurantSearchCondition searchCondition,
        Coord memberLocation,
        Pageable pageable
    );
}
