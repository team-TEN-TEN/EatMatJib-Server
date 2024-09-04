package com.tenten.eatmatjib.restaurant.domain;

import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_RANGE_TYPE_EXCEPTION;

import com.tenten.eatmatjib.common.exception.BusinessException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RangeType {

    ONE(1),
    FIVE(5),
    TEN(10);

    private final int value;

    private static final Map<Integer, RangeType> rangeTypeMap = Arrays.stream(values())
        .collect(Collectors.toMap(RangeType::getValue, type -> type));

    public static RangeType of(int range) {
        if (rangeTypeMap.containsKey(range)) {
            return rangeTypeMap.get(range);
        } else {
            throw new BusinessException(INVALID_RANGE_TYPE_EXCEPTION);
        }
    }
}
