package com.tenten.eatmatjib.member.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.member.controller.response.InfoMemberRes;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberRepository memberRepository;

    public InfoMemberRes execute(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return InfoMemberRes.of(member);
    }
}
