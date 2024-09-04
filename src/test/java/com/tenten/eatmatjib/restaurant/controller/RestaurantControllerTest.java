package com.tenten.eatmatjib.restaurant.controller;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.GlobalExceptionHandler;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.service.RestaurantQueryService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantQueryService restaurantQueryService;

    @InjectMocks
    private RestaurantController restaurantController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(restaurantController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .alwaysDo(print())
            .build();
    }

    @DisplayName("맛집 목록 조회를 성공하면 200을 반환한다.")
    @Test
    public void restaurantListsSuccessReturn200() throws Exception {
        // given
        Page<RestaurantsQueryRes> response = new PageImpl<>(
            List.of(getRestaurantsQueryRes()),
            PageRequest.of(0, 10),
            1
        );

        when(restaurantQueryService.execute(any(), any(), any(), any())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/restaurants")
                .param("lat", "37.5665")
                .param("lon", "126.9780")
                .param("range", "1")
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(response.getContent().get(0).getId()));
    }

    @DisplayName("존재하지 않는 멤버 id로 맛집 목록 조회를 하면 404를 반환한다.")
    @Test
    public void notFoundMemberRestaurantListsReturn404() throws Exception {
        // given
        when(restaurantQueryService.execute(any(), any(), any(), any()))
            .thenThrow(new BusinessException(MEMBER_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/restaurants")
                .param("lat", "37.5665")
                .param("lon", "126.9780")
                .param("range", "1")
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("존재하지 않는 멤버입니다."));
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
}
