package com.shopping_app.shoppingApp.model.Cart;

import com.shopping_app.shoppingApp.domain.CartItems;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartAddResponse {

    private long id;
    private CartItemResponse cartItem;

    public static CartAddResponse from(long cartId, CartItems cartItems) {
        return CartAddResponse.builder().id(cartId).cartItem(CartItemResponse.from(cartItems)).build();
    }

}
