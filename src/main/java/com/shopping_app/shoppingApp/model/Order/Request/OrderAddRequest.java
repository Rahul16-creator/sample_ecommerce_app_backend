package com.shopping_app.shoppingApp.model.Order.Request;

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
public class OrderAddRequest {

    @NotBlank
    private Long cartId;

    @NotBlank
    private Long shippingAddressId;
}