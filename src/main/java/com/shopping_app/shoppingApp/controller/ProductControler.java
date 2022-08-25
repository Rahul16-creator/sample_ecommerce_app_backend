package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import com.shopping_app.shoppingApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product/")
@Validated
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductControler {
//
//    private final ProductService productService;
//
//    @GetMapping("/")
//    public ResponseEntity<ApiResponse> fetchAllProducts() {
//        List<ProductResponse> allProducts = productService.getAllProducts();
//        ApiResponse<List<ProductResponse>> Response = new ApiResponse<List<ProductResponse>>(HttpStatus.OK, "All Product Fetched Successfully", allProducts.size(), ResponseType.SUCCESS, allProducts);
//        return new ResponseEntity<>(Response, Response.getCode());
//
//    }
//
//    @GetMapping("/{productId}")
//    public ResponseEntity<ApiResponse> fetchProductById(@PathVariable Long productId) {
//        ProductResponse productResponse = productService.getProductById(productId);
//        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.OK, "Product Fetched Successfully", 1, ResponseType.SUCCESS, productResponse);
//        return new ResponseEntity<>(Response, Response.getCode());
//
//    }
//
//    // todo add @valid handle exception in exception class
//    @PostMapping("/")
//    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
//        ProductResponse productResponse = productService.addProduct(productRequest);
//        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.BAD_REQUEST, "Product added Successfully", 1, ResponseType.SUCCESS, productResponse);
//        return new ResponseEntity<>(Response, Response.getCode());
//
//    }
//


}
