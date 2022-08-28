package com.shopping_app.shoppingApp.model.Product.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank
    private String productName;

    @NotNull
    private Float price;

    @NotBlank
    private String description;

    @NotNull
    private Integer availableQuantity;
}