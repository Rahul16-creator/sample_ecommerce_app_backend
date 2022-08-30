package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}