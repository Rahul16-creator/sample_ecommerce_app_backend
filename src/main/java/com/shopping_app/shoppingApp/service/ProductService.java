package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Product.ProductResponse;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService {

    private final ProductRepository productRepository;
    private final WebSocketService webSocketService;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        webSocketService.publishProductCount(products.size());
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        return ProductResponse.from(product);
    }

    public Product findProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new NotFoundException("Product not found", HttpStatus.NOT_FOUND);
        }
        return product.get();
    }
}