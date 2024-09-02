package com.tenten.eatmatjib.restaurant.repository.custom;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.RestaurantRepository;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class RestaurantCustomRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @DisplayName("사용자가 검색시 페이징된 맛집 검색 결과 목록을 반환한다.")
    @Test
    void memberSearchReturnPageRestaurantsQueryRes() {
        // given
        Restaurant restaurant1 = new Restaurant(1L, "맛있는 식당 A", "12345", "서울특별시 중구 무슨동 1-1", "일식",
            BigDecimal.valueOf(441275), BigDecimal.valueOf(640356), "010-1234-5678",
            "http://sushi.com", BigDecimal.valueOf(4.5), 120, LocalDateTime.now());
        Restaurant restaurant2 = new Restaurant(2L, "맛있는 식당 B", "67890", "서울특별시 중구 무슨동 2-2", "한식",
            BigDecimal.valueOf(441265), BigDecimal.valueOf(640355), "010-9876-5432",
            "http://pizza.com", BigDecimal.valueOf(4.7), 120, LocalDateTime.now());
        restaurantRepository.saveAll(List.of(restaurant1, restaurant2));

        Coord memberLocation = new Coord(442075, 639756);
        RestaurantSearchCondition searchCondition = RestaurantSearchCondition.of(1, "", "", "");

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<RestaurantsQueryRes> result =
            restaurantRepository.search(searchCondition, memberLocation, pageRequest);

        // then
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("맛있는 식당 A");
    }
}