package com.shopping_app.shoppingApp.service.impl;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.model.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import com.shopping_app.shoppingApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, @Qualifier("productMapperImpl") ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = productMapper.convertToProductDomain(productRequest);
        productRepository.save(product);
        return productMapper.convertToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productsResponse = products.
                stream()
                .map(productMapper::convertToProductResponse)
                .collect(Collectors.toList());
        return productsResponse;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = fetchProductById(id);
        ProductResponse productResponse = productMapper.convertToProductResponse(product);
        return productResponse;
    }

    @Override
    public ProductResponse updateProductById(ProductRequest productRequest, Long id) {
        Product product = fetchProductById(id);
        if (StringUtils.isNotBlank(productRequest.getProductName())) {
            product.setProductName(productRequest.getProductName());
        }
        if (StringUtils.isNotBlank(productRequest.getDescription())) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getPrice() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getAvailableQuantity() != null) {
            product.setAvailableQuantity(productRequest.getAvailableQuantity());
        }
        Product updatedProduct = productRepository.save(product);
        return productMapper.convertToProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse deleteProductById(Long id) {
        Product product = fetchProductById(id);
        productRepository.deleteById(id);
        return productMapper.convertToProductResponse(product);
    }

    public Product fetchProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new NotFoundException("Product Not Found Please check product id", HttpStatus.NOT_FOUND);
        }
        return product.get();
    }

}
