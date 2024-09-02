package com.tenten.eatmatjib.region.controller;

import com.tenten.eatmatjib.region.controller.dto.RegionRes;
import com.tenten.eatmatjib.region.service.RegionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Region", description = "지역의 시군구 데이터")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {

    private final RegionQueryService regionQueryService;

    @Operation(summary = "시군구 데이터 목록 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RegionRes.class)))
    })
    @GetMapping("/regions")
    public ResponseEntity<List<RegionRes>> getRegions() {
        List<RegionRes> response = regionQueryService.getRegions();

        return ResponseEntity.ok(response);
    }
}
