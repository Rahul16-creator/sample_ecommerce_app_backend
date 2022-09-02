package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.CartAddResponse;
import com.shopping_app.shoppingApp.model.Cart.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class CartControllerTest extends AbstractControllerTest {

    private String USER_ID;
    private String CART_ID;
    private ResponseEntity<ApiResponse> CART_RESPONSE_ENTITY;

    @Before
    public void setUp() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        USER_ID = String.valueOf(user.get("id"));
        Optional<Cart> userCart = cartRepository.findByUserId(Long.valueOf(USER_ID));
        CART_ID = String.valueOf(userCart.get().getId());
        CART_RESPONSE_ENTITY = addItemInCart(USER_ID);
        Map<String, Object> cart = getResponseObjectData(CART_RESPONSE_ENTITY);
        CART_ID = String.valueOf(cart.get("id"));
    }

    @Test
    public void testAddCartItem_Success() {
        ResponseEntity<ApiResponse> response1 = CART_RESPONSE_ENTITY;
        assertEquals(HttpStatus.CREATED.value(), response1.getStatusCodeValue());
    }

    @Test
    public void testAddCartItem_Failure_InvalidUser() {
        ResponseEntity<ApiResponse> response = addItemInCart("-100");
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testAddCartItem_Failure_StockExist() {
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        request.setQuantity(1000);
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItems", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetCart_Success() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart", HttpMethod.GET, entity, ApiResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Cart fetched Successfully", response.getBody().getMessage());
    }

    @Test
    public void testGetCart_Failure_InvalidUser() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/-100/cart", HttpMethod.GET, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testGetCart_Failure_TokenMissing() {
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart", HttpMethod.GET, null, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
    }

    @Test
    public void testUpdateCartItem_Success() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());

        ResponseEntity<ApiResponse> response1 = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        assertEquals("Cart Item updated Successfully", response1.getBody().getMessage());
    }

    @Test
    public void testUpdateCartItem_Failure_InvalidUser() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());


        ResponseEntity<ApiResponse> response = httpCall("/users/-100/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testUpdateCartItem_Failure_InvalidCart() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/-1/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Invalid cart id!!", response.getBody().getMessage());
    }

    @Test
    public void testUpdateCartItem_Failure_InvalidCartItem() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());


        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItem/-1", HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("CartItem not found", response.getBody().getMessage());
    }

    @Test
    public void testUpdateCartItem_Failure_StockExists() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        cartItemUpdateRequestPayload.setQuantity(1000);
        HttpEntity<CartItemUpdateRequest> entity = getEntity(cartItemUpdateRequestPayload, getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteCartItem_Success() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteCartItem_Failure_InvalidUser() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/-100/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testDeleteCartItem_Failure_InvalidCart() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        Map<String, Object> cartItem = (Map<String, Object>) cartResponse.get("cartItem");
        String carItemId = String.valueOf(cartItem.get("id"));

        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/-1/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Invalid cart id!!", response.getBody().getMessage());
    }

    @Test
    public void testDeleteCartItem_Failure_InvalidCartItem() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/cart/" + CART_ID + "/cartItem/-1", HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("CartItem not found", response.getBody().getMessage());
    }

    public ResponseEntity<ApiResponse> addItemInCart(String userId) {
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        return httpCall("/users/" + userId + "/cart/" + CART_ID + "/cartItems", HttpMethod.POST, entity, ApiResponse.class);
    }

}