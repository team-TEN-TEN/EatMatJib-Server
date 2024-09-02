package com.tenten.eatmatjib.restaurant.service;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.ErrorCode;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.review.domain.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantQueryServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantQueryService restaurantQueryService;

    private Restaurant restaurant;
    private Member member;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 음식점 엔티티 생성
        restaurant = Restaurant.builder()
                .id(1L)
                .name("맛집")
                .zipCode("12345")
                .address("서울시 강남구")
                .cuisine("한식")
                .x(BigDecimal.valueOf(127.12345))
                .y(BigDecimal.valueOf(37.56789))
                .phoneNumber("010-1234-5678")
                .homepageUrl("http://맛집.com")
                .avgScore(BigDecimal.valueOf(4.5))
                .viewCount(100)
                .updatedAt(LocalDateTime.now())
                .build();

        // 회원 엔티티 생성
        member = Member.builder()
                .account("John Doe")
                .password("password123")
                .joinedAt(LocalDateTime.now())
                .build();

        // 첫 번째 리뷰 엔티티 생성 (최근 리뷰)
        review1 = Review.builder()
                .content("정말 맛있어요!")
                .score(5)
                .createdAt(LocalDateTime.now()) // 최신 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        // 두 번째 리뷰 엔티티 생성 (오래된 리뷰)
        review2 = Review.builder()
                .content("그저 그랬어요.")
                .score(3)
                .createdAt(LocalDateTime.now().minusDays(1)) // 하루 전 시간
                .member(member)
                .restaurant(restaurant)
                .build();

        restaurant.addReview(review1);
        restaurant.addReview(review2);


    }

    @DisplayName("정상 케이스 (200). 리뷰가 최신 순으로 정렬되었는지 확인.")
    @Test
    void getRestaurantDetail_whenRestaurantExists_thenReturnRestaurantWithReviewsInDescendingOrder() {
        // given
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // when
        Restaurant result = restaurantQueryService.getRestaurantDetail(restaurantId);

        // then
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        assertFalse(result.getReviews().isEmpty());
        assertEquals(2, result.getReviews().size());

        // 리뷰가 최신순으로 정렬되었는지 검증
        assertEquals(review1.getCreatedAt(), result.getReviews().get(0).getCreatedAt());
        assertEquals(review2.getCreatedAt(), result.getReviews().get(1).getCreatedAt());
        assertTrue(result.getReviews().get(0).getCreatedAt()
                .isAfter(result.getReviews().get(1).getCreatedAt()));
    }

    @DisplayName("restaurantId가 존재하지 않는 경우 404 에러")
    @Test
    void getRestaurantDetail_whenRestaurantDoesNotExist_thenThrowException() {
        // given
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> restaurantQueryService.getRestaurantDetail(restaurantId));

        // 예외에 올바른 오류 코드가 포함되어 있는지 확인
        assertEquals(ErrorCode.RESTAURANT_NOT_FOUND, exception.getErrorCode());
    }
}
=======
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
class RestaurantQueryServiceTest {

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
>>>>>>> 4369e00cc8c3f00b101d70f4e56aecfe8f0a320d
