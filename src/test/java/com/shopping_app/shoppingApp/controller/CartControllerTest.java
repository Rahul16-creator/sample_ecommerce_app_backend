package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
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
        CART_RESPONSE_ENTITY = addItemInCart(USER_ID);
        Map<String, Object> cart = getResponseObjectData(CART_RESPONSE_ENTITY);
        CART_ID = String.valueOf(cart.get("id"));
    }

    @Test
    public void testAddCartItem() {
        ResponseEntity<ApiResponse> response1 = CART_RESPONSE_ENTITY;
        assertEquals(HttpStatus.CREATED.value(), response1.getStatusCodeValue());

        ResponseEntity<ApiResponse> response2 = addItemInCart("-100");
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        request.setQuantity(1000);
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + USER_ID + "/cart/add", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatusCodeValue());
    }


    @Test
    public void testGetCart() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());

        ResponseEntity<ApiResponse> response1 = httpCall("/user/" + USER_ID + "/cart", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        assertEquals("Cart fetched Successfully", response1.getBody().getMessage());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/-100/cart", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + USER_ID + "/cart", HttpMethod.GET, null, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response3.getStatusCodeValue());
    }

    @Test
    public void testUpdateCartItem() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        List cartItems = (List) cartResponse.get("cartItems");
        Map<String, Object> cartItemResponse = (Map<String, Object>) cartItems.get(0);
        String carItemId = String.valueOf(cartItemResponse.get("id"));

        CartItemUpdateRequest cartItemUpdateRequestPayload = MockPayload.getCartItemUpdateRequestPayload();
        HttpEntity<Object> entity = getEntity(cartItemUpdateRequestPayload, getHeader());
        ResponseEntity<ApiResponse> response1 = httpCall("/user/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        assertEquals("Cart Item updated Successfully", response1.getBody().getMessage());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/-100/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + USER_ID + "/cart/-1/cartItem/" + carItemId, HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response3.getStatusCodeValue());
        assertEquals("Invalid cart id!!", response3.getBody().getMessage());

        ResponseEntity<ApiResponse> response4 = httpCall("/user/" + USER_ID + "/cart/" + CART_ID + "/cartItem/-1", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response4.getStatusCodeValue());
        assertEquals("CartItem not found", response4.getBody().getMessage());

        cartItemUpdateRequestPayload.setQuantity(1000);
        HttpEntity<CartItemUpdateRequest> entity2 = getEntity(cartItemUpdateRequestPayload, getHeader());
        ResponseEntity<ApiResponse> response5 = httpCall("/user/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.PUT, entity2, ApiResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response5.getStatusCodeValue());
    }

    @Test
    public void testDeleteCartItem() {
        Map<String, Object> cartResponse = getResponseObjectData(CART_RESPONSE_ENTITY);
        List cartItems = (List) cartResponse.get("cartItems");
        Map<String, Object> cartItemResponse = (Map<String, Object>) cartItems.get(0);
        String carItemId = String.valueOf(cartItemResponse.get("id"));


        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + USER_ID + "/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());


        ResponseEntity<ApiResponse> response2 = httpCall("/user/-100/cart/" + CART_ID + "/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + USER_ID + "/cart/-1/cartItem/" + carItemId, HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response3.getStatusCodeValue());
        assertEquals("Invalid cart id!!", response3.getBody().getMessage());

        ResponseEntity<ApiResponse> response4 = httpCall("/user/" + USER_ID + "/cart/" + CART_ID + "/cartItem/-1", HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response4.getStatusCodeValue());
        assertEquals("CartItem not found", response4.getBody().getMessage());
    }

    public ResponseEntity<ApiResponse> addItemInCart(String userId) {
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        return httpCall("/user/" + userId + "/cart/add", HttpMethod.POST, entity, ApiResponse.class);
    }

}