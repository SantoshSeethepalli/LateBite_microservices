package com.backend.restaurant_service.repository;

import com.backend.restaurant_service.model.OperatingStatus;
import com.backend.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    public List<Restaurant> findByOperatingStatus(OperatingStatus operatingStatus);
}
