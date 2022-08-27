package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.Response.CartResponse;

public interface CartService {
    CartResponse getCart();

    CartResponse addItemsInCart(CartAddRequest cartAddRequest);
}
