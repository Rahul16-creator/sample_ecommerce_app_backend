package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, @Qualifier("productMapperImpl") ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = productMapper.convertToProductDomain(productRequest);
        productRepository.save(product);
        return productMapper.convertToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productsResponse = products.
                stream()
                .map(productMapper::convertToProductResponse)
                .collect(Collectors.toList());
        return productsResponse;
    }

    public ProductResponse getProductById(Long id) {
        Product product = fetchProductById(id);
        return productMapper.convertToProductResponse(product);
    }

    public ProductResponse updateProductById(ProductRequest productRequest, Long id) {
        Product product = fetchProductById(id);
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setDescription(productRequest.getDescription());
        product.setAvailableQuantity(productRequest.getAvailableQuantity());
        Product updatedProduct = productRepository.save(product);
        return productMapper.convertToProductResponse(updatedProduct);
    }

    public ProductResponse deleteProductById(Long id) {
        Product product = fetchProductById(id);
        productRepository.deleteById(id);
        return productMapper.convertToProductResponse(product);
    }

    public Product fetchProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Product Not Found Please check product id", HttpStatus.NOT_FOUND);
        }
        return product.get();
    }
}