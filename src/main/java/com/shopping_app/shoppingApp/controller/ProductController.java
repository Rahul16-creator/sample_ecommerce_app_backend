package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Product.ProductResponse;
import com.shopping_app.shoppingApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductResponse> productResponse = productService.getAllProducts();
        return new ResponseEntity<>(getResponse("All the Products Fetched Successfully", productResponse), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(getResponse("Product Fetched Successfully", productResponse), HttpStatus.OK);
    }

    public ApiResponse getResponse(String message, Object data) {
        return ApiResponse.builder()
                .data(data)
                .code(HttpStatus.OK)
                .message(message)
                .status(ResponseType.SUCCESS)
                .build();
    }
}