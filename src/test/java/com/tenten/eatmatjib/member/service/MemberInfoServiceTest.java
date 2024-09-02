package com.tenten.eatmatjib.member.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.member.controller.response.InfoMemberRes;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberInfoServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberInfoService memberInfoService;

    @DisplayName("사용자 정보 조회를 하면 사용자 정보 응답 객체를 반환한다.")
    @Test
    void memberInfoReturnInfoMemberRes() {
        // given
        Long memberId = 1L;
        Member member = getMember();
        InfoMemberRes response = InfoMemberRes.of(getMember());

        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(member));

        // when
        InfoMemberRes result = memberInfoService.execute(memberId);

        // then
        assertThat(result.getId()).isEqualTo(response.getId());
    }

    @DisplayName("존재하지 않는 id로 사용자 정보 조회를 하면 예외가 발생한다.")
    @Test
    void notFoundMemberInfoThrowsException() {
        // given
        Long memberId = 1L;

        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class,
            () -> memberInfoService.execute(memberId)
        );

        // then
        assertThat(exception.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
    }

    private Member getMember() {
        return Member.builder()
            .account("tenten")
            .password("password12!")
            .joinedAt(LocalDateTime.now())
            .build();
    }
}