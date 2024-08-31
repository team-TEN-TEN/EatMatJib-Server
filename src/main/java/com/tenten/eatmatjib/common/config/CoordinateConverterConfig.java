package com.tenten.eatmatjib.common.config;

import com.tenten.eatmatjib.common.util.CoordinateConverter;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoordinateConverterConfig {
    private static final String SOURCE_COORDINATE_SYSTEM = "epsg:4326";
    private static final String TARGET_COORDINATE_SYSTEM = "epsg:2097";

    @Bean
    public CoordinateConverter coordinateConverter() {
        CRSFactory factory = new CRSFactory();

        CoordinateReferenceSystem source = factory.createFromName(SOURCE_COORDINATE_SYSTEM);
        CoordinateReferenceSystem target = factory.createFromName(TARGET_COORDINATE_SYSTEM);

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        return new CoordinateConverter(ctFactory.createTransform(source, target));
    }
}
