package com.shopping_app.shoppingApp.model.Order;

import com.shopping_app.shoppingApp.domain.OrderItems;
import com.shopping_app.shoppingApp.model.Product.ProductResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponse {
    private ProductResponse product;
    private int quantity;
    private double totalPrice;

    public static OrderItemResponse from(OrderItems orderItem) {

        return OrderItemResponse
                .builder()
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .product(ProductResponse.from(orderItem.getProduct()))
                .build();
    }
}