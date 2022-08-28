package com.shopping_app.shoppingApp.model.Cart.Request;

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
public class CartItemUpdateRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private int quantity;
}
