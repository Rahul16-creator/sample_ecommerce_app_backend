package com.shopping_app.shoppingApp.model.Order.Request;

import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderUpdateRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private OrderStatus orderStatus;
}
