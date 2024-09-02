package com.tenten.eatmatjib.region.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tenten.eatmatjib.common.exception.FileNotFoundException;
import com.tenten.eatmatjib.region.domain.Region;
import com.tenten.eatmatjib.region.repository.RegionRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegionImportService {
    private static final String REQUIRED_REGION_NAME = "서울";

    @Value("${custom.file.csv.region.path}")
    private String filePath;

    private final RegionRepository regionRepository;

    public void importRegionData() {
        InputStream inputStream = getClass().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new FileNotFoundException(filePath);
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String[] nextLine;
            csvReader.readNext();

            while ((nextLine = csvReader.readNext()) != null) {
                String city = nextLine[0];
                String district = nextLine[1];

                if (isNotRequiredCity(city)) {
                    continue;
                }

                saveOrUpdate(city, district);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private boolean isNotRequiredCity(String city) {
        return !Objects.equals(city, REQUIRED_REGION_NAME);
    }

    private void saveOrUpdate(String city, String district) {
        Optional<Region> existingRegion = regionRepository.findDistinctByCityAndDistrict(city, district);

        Region region;
        if (existingRegion.isPresent()) {
            region = existingRegion.get();
            region.update(city, district);
        } else {
            region = Region.builder()
                    .city(city)
                    .district(district)
                    .build();
        }

        regionRepository.save(region);
    }
}