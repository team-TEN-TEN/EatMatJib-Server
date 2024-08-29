package com.tenten.eatmatjib.member.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.ACCOUNT_UNAUTHORIZED;
import static com.tenten.eatmatjib.common.exception.ErrorCode.PASSWORD_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tenten.eatmatjib.common.config.auth.JwtToken;
import com.tenten.eatmatjib.common.config.auth.JwtUtil;
import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.member.controller.request.LoginMemberReq;
import com.tenten.eatmatjib.member.controller.response.LoginMemberRes;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MemberLoginService memberLoginService;

    @DisplayName("사용자가 로그인을 하면 사용자 로그인 응답 객체를 반환한다.")
    @Test
    void memberLoginReturnLoginMemberRes() {
        // given
        LoginMemberReq request = getLoginMemberReq();
        Member member = getMember();

        when(memberRepository.findByAccount(any())).thenReturn(Optional.ofNullable(member));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.createToken(any())).thenReturn(
            JwtToken.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build()
        );

        // when
        LoginMemberRes result = memberLoginService.execute(request);

        // then
        assertThat(result.getAccount()).isEqualTo(member != null ? member.getAccount() : null);
    }

    @DisplayName("사용자가 존재하지 않는 계정으로 로그인을 하면 예외가 발생한다.")
    @Test
    void memberAccountUnauthorizedLoginThrowsException() {
        // given
        LoginMemberReq request = getLoginMemberReq();

        when(memberRepository.findByAccount(any())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class,
            () -> memberLoginService.execute(request)
        );

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ACCOUNT_UNAUTHORIZED);
    }

    @DisplayName("사용자가 비밀번호 잘못 입력 후 로그인을 하면 예외가 발생한다.")
    @Test
    void memberPasswordUnauthorizedLoginThrowsException() {
        // given
        LoginMemberReq request = getLoginMemberReq();

        when(memberRepository.findByAccount(any())).thenReturn(Optional.ofNullable(getMember()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // when
        BusinessException exception = assertThrows(BusinessException.class,
            () -> memberLoginService.execute(request)
        );

        // then
        assertThat(exception.getErrorCode()).isEqualTo(PASSWORD_UNAUTHORIZED);
    }

    private LoginMemberReq getLoginMemberReq() {
        return LoginMemberReq.builder()
            .account("tenten")
            .password("password12!")
            .build();
    }

    private Member getMember() {
        return Member.builder()
            .account("tenten")
            .email("tenten@gmail.com")
            .password("password12!")
            .joinedAt(LocalDateTime.now())
            .build();
    }

    private Member getPreMember() {
        return Member.builder()
            .account("tenten2")
            .email("tenten2@gmail.com")
            .password("password12!")
            .joinedAt(LocalDateTime.now())
            .build();
    }
}