package com.backend.restaurant_service.repository;

import com.backend.restaurant_service.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
