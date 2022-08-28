package com.shopping_app.shoppingApp.model.Order.Response;

import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private AddressResponse shippingAddress;
    private Set<OrderItemResponse> orderItems;
    private OrderStatus orderStatus;
    private String trackingNumber;
    private LocalDate deliveryDate;
}
