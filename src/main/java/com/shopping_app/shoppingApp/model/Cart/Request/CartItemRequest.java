package com.shopping_app.shoppingApp.model.Cart.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class CartItemRequest {

    @NotNull
    private Long product_id;

    @NotNull
    private int quantity;
}