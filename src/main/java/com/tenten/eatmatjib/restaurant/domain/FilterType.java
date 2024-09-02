package com.tenten.eatmatjib.restaurant.domain;

import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_FILTER_TYPE_EXCEPTION;

import com.tenten.eatmatjib.common.exception.BusinessException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {

    KOREAN("한식"),
    CHINESE("중국식"),
    JAPANESE("일식"),
    ETC("기타");

    private final String value;

    private static final Map<String, FilterType> filterTypeMap = Arrays.stream(values())
        .collect(Collectors.toMap(FilterType::getValue, type -> type));

    public static FilterType of(String filterBy) {
        if (filterBy == null || filterBy.isBlank()) {
            return null;
        }
        if (filterTypeMap.containsKey(filterBy)) {
            return filterTypeMap.get(filterBy);
        } else {
            throw new BusinessException(INVALID_FILTER_TYPE_EXCEPTION);
        }
    }
}
