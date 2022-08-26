package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.model.Request.ProductRequest;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Before
    public void addMockData() {

    }

    @Test
    public void testAddProductSuccess() {
        ProductRequest request = ProductRequest.builder().productName("test_product").availableQuantity(1).price(10.1F).description("test description").build();
        Product product = productMapper.convertToProductDomain(request);
        Product response = productRepository.save(product);
        assertEquals(request.getProductName(), response.getProductName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(Optional.ofNullable(request.getAvailableQuantity()), response.getAvailableQuantity());
        assertEquals(request.getPrice(), response.getPrice());
    }

    @Test(expected = NullPointerException.class)
    public void testAddEmptyProduct() {
        ProductRequest request = ProductRequest.builder().build();
        Product product = productMapper.convertToProductDomain(request);
        productRepository.save(product);
    }

    @Test
    public void testGetAllProductsSuccess() {
        addMockProducts();
        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 5);
    }

    @Test
    public void testGetAllProductsEmpty() {
        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 0);
    }

    @After
    public void clearMockDbData() {
        productRepository.deleteAll();
    }

    public void addMockProducts() {
        ProductRequest request1 = ProductRequest.builder().productName("test_product1").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request2 = ProductRequest.builder().productName("test_product2").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request3 = ProductRequest.builder().productName("test_product3").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request4 = ProductRequest.builder().productName("test_product4").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request5 = ProductRequest.builder().productName("test_product5").availableQuantity(1).price(10.1F).description("test description").build();
        Product product1 = productMapper.convertToProductDomain(request1);
        Product product2 = productMapper.convertToProductDomain(request2);
        Product product3 = productMapper.convertToProductDomain(request3);
        Product product4 = productMapper.convertToProductDomain(request4);
        Product product5 = productMapper.convertToProductDomain(request5);
        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));
    }
}
