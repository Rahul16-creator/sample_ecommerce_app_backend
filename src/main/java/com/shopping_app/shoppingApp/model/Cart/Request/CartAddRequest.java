package com.shopping_app.shoppingApp.model.Cart.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartAddRequest {
    public CartItemRequest cartItemRequest;

    public CartItemRequest getCartItemRequest() {
        return cartItemRequest;
    }

    public void setCartItemRequest(CartItemRequest cartItemRequest) {
        this.cartItemRequest = cartItemRequest;
    }
}

