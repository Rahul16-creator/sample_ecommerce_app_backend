package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import com.shopping_app.shoppingApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product/")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductControler {

    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> fetchAllProducts() {
        List<ProductResponse> allProducts = productService.getAllProducts();
        ApiResponse<List<ProductResponse>> Response = new ApiResponse<List<ProductResponse>>(HttpStatus.OK, "All Product Fetched Successfully", ResponseType.SUCCESS, allProducts);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> fetchProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.OK, "Product Fetched Successfully", ResponseType.SUCCESS, productResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.addProduct(productRequest);
        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.OK, "Product added Successfully", ResponseType.SUCCESS, productResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@Valid @RequestBody ProductRequest productRequest, @PathVariable Long id) {
        ProductResponse productResponse = productService.updateProductById(productRequest, id);
        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.OK, "Product added Successfully", ResponseType.SUCCESS, productResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        ProductResponse productResponse = productService.deleteProductById(id);
        ApiResponse<ProductResponse> Response = new ApiResponse<ProductResponse>(HttpStatus.OK, "Product deleted Successfully", ResponseType.SUCCESS, productResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }
}
