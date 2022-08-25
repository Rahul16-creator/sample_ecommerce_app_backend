package com.shopping_app.shoppingApp.model.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRequest {
    List<CartItemRequest> cartItemRequestList;

    public List<CartItemRequest> getCartItemRequestList() {
        return cartItemRequestList;
    }

    public void setCartItemRequestList(List<CartItemRequest> cartItemRequestList) {
        this.cartItemRequestList = cartItemRequestList;
    }
}
