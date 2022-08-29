package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.CartItems;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CartItemRepository extends AbstractRepository<CartItems> {

    @Modifying
    @Query("DELETE FROM CartItems n WHERE n.id = :id")
    void deleteCartItems(Long id);
}
