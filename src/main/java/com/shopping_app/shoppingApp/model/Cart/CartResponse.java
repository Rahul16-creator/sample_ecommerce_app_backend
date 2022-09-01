package com.shopping_app.shoppingApp.model.Cart;

import com.shopping_app.shoppingApp.domain.Cart;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartResponse {
    private Long id;
    private Set<CartItemResponse> cartItems;

    public static CartResponse from(Cart cart, Set<CartItemResponse> cartItemResponses) {
        return CartResponse.builder().id(cart.getId()).cartItems(cartItemResponses).build();
    }
}
