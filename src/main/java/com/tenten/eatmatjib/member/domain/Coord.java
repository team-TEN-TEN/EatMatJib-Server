package com.tenten.eatmatjib.member.domain;

import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_LATITUDE_EXCEPTION;
import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_LONGITUDE_EXCEPTION;

import com.tenten.eatmatjib.common.exception.BusinessException;

public record Coord(
    double x,
    double y
) {

    public static Coord of(double lat, double lon) {
        if (!(-90 <= lat && lat <= 90)) {
            throw new BusinessException(INVALID_LATITUDE_EXCEPTION);
        }
        if (!(-180 <= lon && lon <= 180)) {
            throw new BusinessException(INVALID_LONGITUDE_EXCEPTION);
        }
        return new Coord(lat, lon);
    }
}
