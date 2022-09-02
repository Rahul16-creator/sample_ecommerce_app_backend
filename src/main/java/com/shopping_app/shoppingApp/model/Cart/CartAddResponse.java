package com.shopping_app.shoppingApp.model.Cart;

import com.shopping_app.shoppingApp.domain.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartAddResponse {

    private long id;
    private CartItemResponse cartItem;

    public static CartAddResponse from(long cartId, CartItem cartItems) {
        return CartAddResponse.builder().id(cartId).cartItem(CartItemResponse.from(cartItems)).build();
    }

}
