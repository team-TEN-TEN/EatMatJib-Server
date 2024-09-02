package com.tenten.eatmatjib.restaurant.service.command;

import com.tenten.eatmatjib.restaurant.domain.FilterType;
import com.tenten.eatmatjib.restaurant.domain.OrderType;
import com.tenten.eatmatjib.restaurant.domain.RangeType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantSearchCondition {

    private RangeType rangeType;
    private String keyword;
    private FilterType filterType;
    private OrderType orderType;

    public static RestaurantSearchCondition of(
        int range,
        String keyword,
        String filterBy,
        String orderBy
    ) {
        return RestaurantSearchCondition.builder()
            .rangeType(RangeType.of(range))
            .keyword(keyword)
            .filterType(FilterType.of(filterBy))
            .orderType(OrderType.of(orderBy))
            .build();
    }
}
