package com.tenten.eatmatjib.region.repository;

import com.tenten.eatmatjib.region.domain.Region;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findDistinctByCityAndDistrict(String city, String district);
}
