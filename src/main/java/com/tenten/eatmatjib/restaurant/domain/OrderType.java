package com.tenten.eatmatjib.restaurant.domain;

import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_ORDER_TYPE_EXCEPTION;

import com.tenten.eatmatjib.common.exception.BusinessException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {

    DISTANCE_RATE("distance,rate"),
    DISTANCE("distance"),
    RATE("rate");

    private final String value;

    private static final Map<String, OrderType> orderTypeMap = Arrays.stream(values())
        .collect(Collectors.toMap(OrderType::getValue, type -> type));

    public static OrderType of(String orderBy) {
        if (orderBy == null || orderBy.isBlank()) {
            return DISTANCE_RATE;
        }
        if (orderTypeMap.containsKey(orderBy)) {
            return orderTypeMap.get(orderBy);
        } else {
            throw new BusinessException(INVALID_ORDER_TYPE_EXCEPTION);
        }
    }
}
