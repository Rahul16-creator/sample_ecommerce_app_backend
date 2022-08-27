package com.shopping_app.shoppingApp.model.Cart.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse {

    private long id;
    private ProductResponse productResponse;
    private int quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
