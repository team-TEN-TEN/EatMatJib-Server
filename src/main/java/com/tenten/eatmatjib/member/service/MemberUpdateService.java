package com.tenten.eatmatjib.member.service;

import static com.tenten.eatmatjib.common.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.tenten.eatmatjib.common.exception.BusinessException;
import com.tenten.eatmatjib.common.util.CoordinateConverter;
import com.tenten.eatmatjib.member.controller.request.UpdateMemberReq;
import com.tenten.eatmatjib.member.controller.response.UpdateMemberRes;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.member.domain.Member;
import com.tenten.eatmatjib.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberUpdateService {

    private final MemberRepository memberRepository;
    private final CoordinateConverter coordinateConverter;

    @Transactional
    public UpdateMemberRes execute(Long memberId, UpdateMemberReq request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        Coord coord = new Coord(request.getLat(), request.getLon());
        Coord convertedCoord = coordinateConverter.convert(coord);

        member.updateInfo(convertedCoord, request.getIsRecommendationActive());

        return UpdateMemberRes.of(member);
    }
}
