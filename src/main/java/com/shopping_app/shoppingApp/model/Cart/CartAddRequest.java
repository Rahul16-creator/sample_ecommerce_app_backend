package com.shopping_app.shoppingApp.model.Cart;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartAddRequest {

    @NotNull
    private Long product_id;

    @NotNull
    private Integer quantity;
}