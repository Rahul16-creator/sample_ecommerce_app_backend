package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.model.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest product);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);

    ProductResponse updateProductById(ProductRequest productRequest , Long id);
    ProductResponse deleteProductById(Long id);

}
