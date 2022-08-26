package com.shopping_app.shoppingApp.model.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private Long id;
    
    private String productName;

    private Float productPrice;

    private String productDescription;

    private Integer availableQuantity;
}
