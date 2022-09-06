package com.shopping_app.shoppingApp.model.Order;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderAddRequest {

    @NotNull
    private Long cartId;

    @NotNull
    private Long shippingAddressId;
}