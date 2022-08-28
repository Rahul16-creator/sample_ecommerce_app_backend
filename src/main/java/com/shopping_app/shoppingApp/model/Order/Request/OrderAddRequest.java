package com.shopping_app.shoppingApp.model.Order.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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