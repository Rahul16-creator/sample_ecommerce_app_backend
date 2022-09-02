package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
