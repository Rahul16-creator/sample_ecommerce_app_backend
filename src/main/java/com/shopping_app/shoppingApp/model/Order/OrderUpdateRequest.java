package com.shopping_app.shoppingApp.model.Order;

import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderUpdateRequest {

    @NotNull
    private OrderStatus orderStatus;
}