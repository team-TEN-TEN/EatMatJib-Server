package com.tenten.eatmatjib.restaurant.repository;

import com.tenten.eatmatjib.restaurant.domain.Restaurant;
import com.tenten.eatmatjib.restaurant.repository.custom.RestaurantCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
    RestaurantCustomRepository {

}
