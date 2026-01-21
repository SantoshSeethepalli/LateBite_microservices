package com.backend.restaurant_service.utils.customComponents;

import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantFromDto(RestaurantUpdateRequest dto, @MappingTarget Restaurant entity);
}