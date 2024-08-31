package com.tenten.eatmatjib.common.util;

import static org.assertj.core.api.Assertions.*;

import com.tenten.eatmatjib.member.domain.Coord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("[단위 테스트] 좌표계 변환 정확도 테스트")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CoordinateConverterTest {

    @Autowired
    CoordinateConverter coordinateConverter;

    @DisplayName("[성공] 좌표계의 원점 좌표 변환에 성공한다.")
    @Test
    void convert_origin_success() {
        // given
        Coord origin = getOriginGeographicCoords();
        Coord expected = getOriginProjectedCoords();

        // when
        Coord actual = coordinateConverter.convert(origin);

        //then
        double delta = 0.1;     // 허용 오차 (미터 단위)
        assertThat(actual.x()).isCloseTo(expected.x(), within(delta));
        assertThat(actual.y()).isCloseTo(expected.y(), within(delta));
    }

    private Coord getOriginGeographicCoords() {
        return new Coord(127.0, 38.0);
    }

    private Coord getOriginProjectedCoords() {
        return new Coord(200000.0, 500000.0);
    }
}