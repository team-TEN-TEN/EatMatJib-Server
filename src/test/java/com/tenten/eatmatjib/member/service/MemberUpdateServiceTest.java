package com.tenten.eatmatjib.member.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.util.CoordinateConverter;
import com.tenten.eatmatjib.member.controller.request.UpdateMemberReq;
import com.tenten.eatmatjib.member.controller.response.UpdateMemberRes;
import com.tenten.eatmatjib.member.domain.Coord;
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
class MemberUpdateServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CoordinateConverter coordinateConverter;

    @InjectMocks
    private MemberUpdateService memberUpdateService;

    @DisplayName("사용자 설정 업데이트를 하면 사용자 설정 업데이트 응답 객체를 반환한다.")
    @Test
    void memberUpdateInfoSuccess() {
        // given
        Long memberId = 1L;
        UpdateMemberReq request = getUpdateMemberReq();
        Member member = getMember();

        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(member));

        when(coordinateConverter.convert(any()))
            .thenReturn(new Coord(4412749.470141057, 6403568.816734589));

        // when
        UpdateMemberRes result = memberUpdateService.execute(memberId, request);

        // then
        assertThat(result.getX()).isEqualTo(member != null ? member.getX() : null);
        assertThat(result.getY()).isEqualTo(member != null ? member.getY() : null);
        assertThat(result.getIsRecommendationActive())
            .isEqualTo(member != null ? member.getIsRecommendationActive() : null);
    }

    @DisplayName("존재하지 않는 id로 사용자 설정 업데이트를 하면 예외가 발생한다.")
    @Test
    void notFoundMemberUpdateInfoThrowsException() {
        // given
        Long memberId = 1L;
        UpdateMemberReq request = getUpdateMemberReq();

        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class,
            () -> memberUpdateService.execute(memberId, request)
        );

        // then
        assertThat(exception.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
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