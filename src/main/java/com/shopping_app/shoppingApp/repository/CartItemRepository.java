package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByIdAndCartId(Long cartItemId, Long cartId);

    Optional<CartItems> findByCartIdAndProductId(Long cartId, Long productId);
}
