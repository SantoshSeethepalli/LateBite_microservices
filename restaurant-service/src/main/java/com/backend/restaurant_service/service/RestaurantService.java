package com.backend.restaurant_service.service;


import com.backend.restaurant_service.utils.customComponents.RestaurantMapper;
import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.repository.RestaurantRepository;
import com.backend.restaurant_service.utils.Mappers.RestaurantMappers;
import com.backend.restaurant_service.utils.dto.CreateRestaurantRequest;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import com.backend.restaurant_service.utils.dto.admin.RestaurantResponse;
import com.backend.restaurant_service.utils.exceptions.exps.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public Long createRestaurant(CreateRestaurantRequest request) {

        Restaurant newRestaurant = restaurantRepository.save(RestaurantMappers.fromCreateRestaurantRequest(request));

        return newRestaurant.getId();
    }

    @Transactional
    public void updateSettings(Long id, RestaurantUpdateRequest request) {

            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new RestaurantNotFoundException("No restaurant with id :" + id));

            restaurantMapper.updateRestaurantFromDto(request, restaurant);

            restaurantRepository.save(restaurant);
    }

    public List<RestaurantResponse> getAll() {

        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }

    public void delete(Long id) {

        restaurantRepository.deleteById(id);
    }

}
