package com.shopping_app.shoppingApp.model.Cart.Response;

import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
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
}
