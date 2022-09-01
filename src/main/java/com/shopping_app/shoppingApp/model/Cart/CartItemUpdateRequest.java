package com.shopping_app.shoppingApp.model.Cart;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemUpdateRequest {

    @NotNull
    private Integer quantity;
}
