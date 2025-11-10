package com.backend.restaurant_service.service;


import com.backend.restaurant_service.mapper.RestaurantMapper;
import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.repository.RestaurantRepository;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;


    public void updateSettings(Long id, RestaurantUpdateRequest request) {

            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No restaurant with id :" + id));

            restaurantMapper.updateRestaurantFromDto(request, restaurant);

            restaurantRepository.save(restaurant);
    }
}
