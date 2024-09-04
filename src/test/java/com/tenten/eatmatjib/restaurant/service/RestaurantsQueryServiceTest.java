package com.tenten.eatmatjib.restaurant.service;
import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.util.CoordinateConverter;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class RestaurantsQueryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CoordinateConverter coordinateConverter;

    @InjectMocks
    private RestaurantQueryService restaurantQueryService;

    @DisplayName("맛집 목록 조회를 하면 페이징된 맛집 목록 응답 객체를 반환한다.")
    @Test
    void restaurantListsReturnPageRestaurantsQueryRes() {
        // given
        Long memberId = 1L;
        RestaurantSearchCondition searchCondition =
            RestaurantSearchCondition.of(1, "치카바", "일식", "distance,rate");
        Coord memberLocation = Coord.of(37.5665, 126.9780);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<RestaurantsQueryRes> response =
            new PageImpl<>(List.of(getRestaurantsQueryRes()), pageable, 1);

        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(getMember()));
        when(coordinateConverter.convert(any()))
            .thenReturn(new Coord(4412749.470141057, 6403568.816734589));
        when(restaurantRepository.search(any(), any(), any())).thenReturn(response);

        // when
        Page<RestaurantsQueryRes> result = restaurantQueryService.execute(
            memberId, searchCondition, memberLocation, pageable
        );

        // then
        assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 멤버 id로 맛집 목록 조회를 하면 예외가 발생한다.")
    @Test
    void notFoundMemberRestaurantListsThrowsException() {
        // given
        Long memberId = 1L;
        RestaurantSearchCondition searchCondition =
            RestaurantSearchCondition.of(1, "치카바", "일식", "distance,rate");
        Coord memberLocation = Coord.of(37.5665, 126.9780);
        PageRequest pageable = PageRequest.of(0, 10);

        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class,
            () -> restaurantQueryService
                .execute(memberId, searchCondition, memberLocation, pageable)
        );

        // then
        assertThat(exception.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
    }

    private RestaurantsQueryRes getRestaurantsQueryRes() {
        return RestaurantsQueryRes.builder()
            .id(1L)
            .name("치카바")
            .address("서울특별시 송파구 송이로20길 12-1, 1층 101호 (가락동)")
            .zipCode("05712")
            .cuisine("일식")
            .phoneNumber("02 448 6648")
            .homepageUrl("https://chikaba.com")
            .avgScore(BigDecimal.valueOf(4.5))
            .build();
    }

    private Member getMember() {
        return Member.builder()
            .account("tenten")
            .password("password12!")
            .joinedAt(LocalDateTime.now())
            .build();
    }
}
