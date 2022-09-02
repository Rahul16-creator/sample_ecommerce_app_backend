package com.shopping_app.shoppingApp.model.Cart;

import com.shopping_app.shoppingApp.domain.CartItem;
import com.shopping_app.shoppingApp.model.Product.ProductResponse;
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
public class CartItemResponse {

    private long id;
    private ProductResponse productResponse;
    private int quantity;

    public static CartItemResponse from(CartItem cartItems) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setProductResponse(ProductResponse.from(cartItems.getProduct()));
        cartItemResponse.setId(cartItems.getId());
        cartItemResponse.setQuantity(cartItems.getQuantity());
        return cartItemResponse;
    }
}
