package com.shopping_app.shoppingApp.model.Order.Response;

import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponse {
    private ProductResponse product;
    private int quantity;
    private Float totalPrice;
}
