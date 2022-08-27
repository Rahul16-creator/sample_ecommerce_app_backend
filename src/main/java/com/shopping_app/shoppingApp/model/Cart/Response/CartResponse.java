package com.shopping_app.shoppingApp.model.Cart.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.model.Cart.Response.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private Set<CartItemResponse> cartItems;

    public Set<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }
}
