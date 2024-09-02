package com.tenten.eatmatjib.region.controller.dto;

import lombok.Builder;

@Builder
public record RegionRes(
        String city,
        String district
) {

}
