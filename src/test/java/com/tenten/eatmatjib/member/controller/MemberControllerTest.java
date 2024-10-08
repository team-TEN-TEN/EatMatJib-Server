package com.tenten.eatmatjib.member.controller;

import static com.tenten.eatmatjib.common.exception.ErrorCode.ACCOUNT_CONFLICT;
import static com.tenten.eatmatjib.common.exception.ErrorCode.ACCOUNT_UNAUTHORIZED;
import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.tenten.eatmatjib.common.exception.ErrorCode.PASSWORD_UNAUTHORIZED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenten.eatmatjib.common.config.auth.JwtToken;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.exception.GlobalExceptionHandler;
import com.tenten.eatmatjib.member.controller.request.LoginMemberReq;
import com.tenten.eatmatjib.member.controller.request.RegisterMemberReq;
import com.tenten.eatmatjib.member.controller.request.UpdateMemberReq;
import com.tenten.eatmatjib.member.controller.response.InfoMemberRes;
import com.tenten.eatmatjib.member.controller.response.LoginMemberRes;
import com.tenten.eatmatjib.member.controller.response.RegisterMemberRes;
import com.tenten.eatmatjib.member.controller.response.UpdateMemberRes;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.service.MemberInfoService;
import com.tenten.eatmatjib.member.service.MemberLoginService;
import com.tenten.eatmatjib.member.service.MemberRegisterService;
import com.tenten.eatmatjib.member.service.MemberUpdateService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberRegisterService memberRegisterService;

    @Mock
    private MemberLoginService memberLoginService;

    @Mock
    private MemberInfoService memberInfoService;

    @Mock
    private MemberUpdateService memberUpdateService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(memberController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .alwaysDo(print())
                .build();
    }

    @DisplayName("사용자가 회원가입을 성공하면 201을 반환한다.")
    @Test
    public void memberRegisterSuccessReturn201() throws Exception {
        // given
        RegisterMemberReq request = getRegisterMemberReq();
        Member member = getMember();
        RegisterMemberRes response = RegisterMemberRes.of(member);

        when(memberRegisterService.execute(any())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()));
    }

    @DisplayName("사용자가 이미 사용중인 계정으로 회원가입을 하면 409를 반환한다.")
    @Test
    void memberAccountConflictRegisterReturn409() throws Exception {
        // given
        RegisterMemberReq request = getRegisterMemberReq();

        when(memberRegisterService.execute(any()))
                .thenThrow(new BusinessException(ACCOUNT_CONFLICT));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 사용중인 계정입니다."));
    }

    @DisplayName("사용자가 로그인을 성공하면 200을 반환한다.")
    @Test
    public void memberLoginSuccessReturn200() throws Exception {
        // given
        LoginMemberReq request = getLoginMemberReq();
        LoginMemberRes response = LoginMemberRes.of(getMember(), JwtToken.builder().build());

        when(memberLoginService.execute(any())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(response.getAccount()))
                .andExpect(jsonPath("$.accessToken").value(response.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(response.getRefreshToken()));
    }

    @DisplayName("사용자가 존재하지 않는 계정으로 로그인을 하면 401을 반환한다.")
    @Test
    void memberAccountUnauthorizedLoginReturn401() throws Exception {
        // given
        LoginMemberReq request = getLoginMemberReq();

        when(memberLoginService.execute(any())).thenThrow(
                new BusinessException(ACCOUNT_UNAUTHORIZED)
        );

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("존재하지 않는 계정입니다."));
    }

    @DisplayName("사용자가 비밀번호 잘못 입력 후 로그인을 하면 401을 반환한다.")
    @Test
    void memberPasswordUnauthorizedLoginReturn401() throws Exception {
        // given
        LoginMemberReq request = getLoginMemberReq();

        when(memberLoginService.execute(any())).thenThrow(
                new BusinessException(PASSWORD_UNAUTHORIZED)
        );

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("비밀번호를 잘못 입력했습니다."));
    }

    @DisplayName("사용자 정보 조회를 성공하면 200을 반환한다.")
    @Test
    public void memberInfoSuccessReturn200() throws Exception {
        // given
        InfoMemberRes response = InfoMemberRes.of(getMember());

        when(memberInfoService.execute(any())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.account").value(response.getAccount()))
                .andExpect(jsonPath("$.x").value(response.getX()))
                .andExpect(jsonPath("$.y").value(response.getY()))
                .andExpect(
                        jsonPath("$.isRecommendationActive").value(response.getIsRecommendationActive())
                ).andExpect(
                        jsonPath("$.joinedAt").value(response.getJoinedAt().toString())
                );
    }

    @DisplayName("존재하지 않는 id로 사용자 정보 조회를 하면 404를 반환한다.")
    @Test
    void notFoundMemberInfoReturn404() throws Exception {
        // given
        when(memberInfoService.execute(any())).thenThrow(new BusinessException(MEMBER_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 멤버입니다."));
    }

    @DisplayName("사용자 설정 업데이트를 성공하면 200을 반환한다.")
    @Test
    void memberUpdateInfoSuccessReturn200() throws Exception {
        // given
        UpdateMemberReq request = getUpdateMemberReq();
        UpdateMemberRes response = UpdateMemberRes.of(getMember());

        when(memberUpdateService.execute(any(), any())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(response.getX()))
                .andExpect(jsonPath("$.y").value(response.getY()))
                .andExpect(
                        jsonPath("$.isRecommendationActive").value(response.getIsRecommendationActive())
                );
    }

    @DisplayName("존재하지 않는 id로 사용자 설정 업데이트를 하면 404를 반환한다.")
    @Test
    void notFoundMemberUpdateInfoReturn404() throws Exception {
        // given
        UpdateMemberReq request = getUpdateMemberReq();

        when(memberUpdateService.execute(any(), any()))
                .thenThrow(new BusinessException(MEMBER_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 멤버입니다."));
    }

    private RegisterMemberReq getRegisterMemberReq() {
        return RegisterMemberReq.builder()
                .account("tenten")
                .password("password12!")
                .build();
    }

    private LoginMemberReq getLoginMemberReq() {
        return LoginMemberReq.builder()
                .account("tenten")
                .password("password12!")
                .build();
    }

    private UpdateMemberReq getUpdateMemberReq() {
        return UpdateMemberReq.builder()
                .lat(37.5665)
                .lon(126.9780)
                .isRecommendationActive(false)
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