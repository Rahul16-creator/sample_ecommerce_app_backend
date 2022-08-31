package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.model.Product.ProductResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testGetAllProducts() {
        List<ProductResponse> productsResponse = productService.getAllProducts();
        assertTrue(productsResponse.size() > 0);
    }

    @Test
    public void testGetProductByIdSuccess() {
        ProductResponse productsResponse = productService.getProductById(1L);
        assertNotNull(productsResponse);
    }

    @Test
    public void testGetProductByIdFailure() {
        try {
            productService.getProductById(-1L);
        } catch (NotFoundException ex) {
            assertEquals("Product not found", ex.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        }
    }
}
