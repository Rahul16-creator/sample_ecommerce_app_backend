package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart>  findByUserId(Long userId);
}