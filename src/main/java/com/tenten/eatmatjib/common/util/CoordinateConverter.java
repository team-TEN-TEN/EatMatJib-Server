package com.tenten.eatmatjib.common.util;

import com.tenten.eatmatjib.member.domain.Coord;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.ProjCoordinate;

public class CoordinateConverter {
    private final CoordinateTransform coordinateTransform;

    public CoordinateConverter(CoordinateTransform coordinateTransform) {
        this.coordinateTransform = coordinateTransform;
    }

    public Coord convert(Coord coord) {
        ProjCoordinate result = new ProjCoordinate();
        coordinateTransform.transform(new ProjCoordinate(coord.x(), coord.y()), result);

        return new Coord(result.x, result.y);
    }
}
