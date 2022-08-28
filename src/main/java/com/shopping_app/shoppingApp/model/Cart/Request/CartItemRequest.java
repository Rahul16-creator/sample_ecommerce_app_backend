package com.shopping_app.shoppingApp.model.Cart.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class CartItemRequest {

    @NotBlank
    private Long product_id;

    @NotBlank
    private int quantity;
}