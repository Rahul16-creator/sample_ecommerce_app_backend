package com.shopping_app.shoppingApp.model.Order;

import com.shopping_app.shoppingApp.domain.Order;
import com.shopping_app.shoppingApp.domain.OrderItems;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static OrderResponse from(Order order) {

        return OrderResponse.builder()
                .orderStatus(order.getStatus())
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .shippingAddress(AddressResponse.from(order.getShippingAddress()))
                .trackingNumber(order.getTrackingNumber())
                .orderItems(getOrderItemResponse(order.getOrderItems())).build();
    }

    public static Set<OrderItemResponse> getOrderItemResponse(Set<OrderItems> orderItems) {
        return orderItems.stream().map(OrderItemResponse::from).collect(Collectors.toSet());
    }
}