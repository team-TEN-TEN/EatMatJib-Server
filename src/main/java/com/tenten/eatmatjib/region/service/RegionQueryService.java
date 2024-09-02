package com.tenten.eatmatjib.region.service;

import com.tenten.eatmatjib.region.controller.dto.RegionRes;
import com.tenten.eatmatjib.region.repository.RegionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionQueryService {

    private final RegionRepository regionRepository;

    public List<RegionRes> getRegions() {
        return regionRepository.findAll()
                .stream()
                .map(region -> RegionRes.builder()
                        .city(region.getSiDo())
                        .district(region.getSgg())
                        .build())
                .toList();
    }
}
