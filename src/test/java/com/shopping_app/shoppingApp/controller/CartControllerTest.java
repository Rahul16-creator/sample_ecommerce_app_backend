package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class CartControllerTest extends AbstractController {

    @Test
    public void testAddCartItem() {
        addUser();
        ResponseEntity<ApiResponse> response = addItemInCart();
        assertEquals(200, response.getStatusCodeValue());
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        request.getCartItemRequest().setProduct_id(1L);
        HttpEntity<CartAddRequest> entity2 = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/cart/", HttpMethod.POST, entity2, ApiResponse.class);
        assertEquals(500, response2.getStatusCodeValue());
    }

    @Test
    public void testGetCart() {
        addUser();
        addItemInCart();
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/cart/", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateCartItem() {
        addUser();
        ResponseEntity<ApiResponse> cart = addItemInCart();
        Map<String, Object> cartResponse = (Map<String, Object>) cart.getBody().getData();
        List cartItems = (List) cartResponse.get("cartItems");
        Map<String, Object> cartItemResponse = (Map<String, Object>) cartItems.get(0);
        String carItemId = String.valueOf(cartItemResponse.get("id"));
        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        cartItemUpdateRequestPayload.setId(Long.valueOf(carItemId));
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/cart/cartItem", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteCartItem() {
        addUser();
        ResponseEntity<ApiResponse> cart = addItemInCart();
        Map<String, Object> cartResponse = (Map<String, Object>) cart.getBody().getData();
        List cartItems = (List) cartResponse.get("cartItems");
        Map<String, Object> cartItemResponse = (Map<String, Object>) cartItems.get(0);
        String carItemId = String.valueOf(cartItemResponse.get("id"));
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/cart/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    public ResponseEntity<ApiResponse> addItemInCart() {
        String id = addMockProduct();
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        request.getCartItemRequest().setProduct_id(Long.valueOf(id));
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        return httpCall("/cart/", HttpMethod.POST, entity, ApiResponse.class);
    }

    public String addMockProduct() {
        ProductRequest productRequest = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(productRequest, getHeader());
        ResponseEntity<ApiResponse> productResponse = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> product = (Map<String, Object>) productResponse.getBody().getData();
        return String.valueOf(product.get("id"));
    }
}