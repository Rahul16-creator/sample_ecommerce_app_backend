package com.shopping_app.shoppingApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductServiceImpl {

//    private final ProductRepository productRepository;
//    private final ProductMapper productMapper;
//
//    @Override
//    public ProductResponse addProduct(ProductRequest productRequest) {
//        Product product = productMapper.convertToProductDomain(productRequest);
//        productRepository.save(product);
//        return productMapper.convertToProductResponse(product);
//    }
//
//    @Override
//    public List<ProductResponse> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        List<ProductResponse> productsResponse = products.
//                stream()
//                .map(productMapper::convertToProductResponse)
//                .collect(Collectors.toList());
//        return productsResponse;
//    }
//
//    @Override
//    public ProductResponse getProductById(Long id) {
//        Product product = fetchProductById(id);
//        ProductResponse productResponse = productMapper.convertToProductResponse(product);
//        return productResponse;
//    }
//
//    public Product fetchProductById(Long id) {
//        Optional<Product> product = productRepository.findById(id);
//        if (!product.isPresent()) {
//            throw new NotFoundException("Product Not Found Please check product id", HttpStatus.NOT_FOUND);
//        }
//        return product.get();
//    }
//

}
