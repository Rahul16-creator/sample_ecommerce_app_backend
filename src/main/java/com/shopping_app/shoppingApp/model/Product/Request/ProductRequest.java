package com.shopping_app.shoppingApp.model.Product.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank
    private String productName;

    @NotBlank
    private Float price;

    @NotBlank
    private String description;

    @NotBlank
    private Integer availableQuantity;
}
