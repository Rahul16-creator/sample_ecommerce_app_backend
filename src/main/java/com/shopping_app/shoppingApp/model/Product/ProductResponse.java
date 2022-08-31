package com.shopping_app.shoppingApp.model.Product;

import com.shopping_app.shoppingApp.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {

    private long id;

    private String productName;

    private double productPrice;

    private String productDescription;

    private int availableQuantity;

    public static ProductResponse from(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .productPrice(product.getPrice())
                .productDescription(product.getDescription())
                .productName(product.getProductName())
                .availableQuantity(product.getAvailableQuantity()).build();
    }
}