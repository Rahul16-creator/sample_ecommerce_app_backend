package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductControllerTest extends AbstractControllerTest {

    @Test
    public void testGetAllProduct() {
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response1 = httpCall("/products", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        assertEquals("All the Products Fetched Successfully", response1.getBody().getMessage());

        ResponseEntity<ApiResponse> response2 = httpCall("/products", HttpMethod.GET, null, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response2.getStatusCodeValue());
    }

    @Test
    public void testGetProductById() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response1 = httpCall("/products/1", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        assertEquals("Product Fetched Successfully", response1.getBody().getMessage());

        ResponseEntity<ApiResponse> response2 = httpCall("/products/100", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), response2.getStatusCodeValue());
        assertEquals("Product not found", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/products", HttpMethod.GET, null, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response3.getStatusCodeValue());
    }
}