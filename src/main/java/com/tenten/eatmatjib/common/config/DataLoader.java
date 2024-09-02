package com.tenten.eatmatjib.common.config;

import com.tenten.eatmatjib.region.service.RegionImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RegionImportService regionImportService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        regionImportService.importRegionData();
    }
}
