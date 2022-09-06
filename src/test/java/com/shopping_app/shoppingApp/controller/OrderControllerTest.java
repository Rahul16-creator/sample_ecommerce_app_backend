package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Order.CreateOrderRequest;
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

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderControllerTest extends AbstractControllerTest {

    private String USER_ID;
    private String CART_ID;
    private String ADDRESS_ID;
    private String ORDER_ID;

    @Before
    public void setUp() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        USER_ID = String.valueOf(user.get("id"));
        Optional<Cart> userCart = cartRepository.findByUserId(Long.valueOf(USER_ID));
        CART_ID = String.valueOf(userCart.get().getId());

        ResponseEntity<ApiResponse> cartResponse = addItemInCart(USER_ID);
        Map<String, Object> cart = getResponseObjectData(cartResponse);
        CART_ID = String.valueOf(cart.get("id"));

        ResponseEntity<ApiResponse> response = addAddress();
        Map<String, Object> addressResponse = getResponseObjectData(response);
        ADDRESS_ID = String.valueOf(addressResponse.get("id"));

        ResponseEntity<ApiResponse> apiResponseResponseEntity = addOrder();
        Map<String, Object> orderResponse = getResponseObjectData(apiResponseResponseEntity);
        ORDER_ID = String.valueOf(orderResponse.get("id"));
    }

    public ResponseEntity<ApiResponse> addItemInCart(String userId) {
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        return httpCall("/users/" + userId + "/cart/" + CART_ID + "/cartItems", HttpMethod.POST, entity, ApiResponse.class);
    }

    public ResponseEntity<ApiResponse> addAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        return httpCall("/users/" + user.get("id") + "/addresses", HttpMethod.POST, entity, ApiResponse.class);
    }

    public ResponseEntity<ApiResponse> addOrder() {
        CreateOrderRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(Long.valueOf(CART_ID));
        orderAddMockerRequest.setShippingAddressId(Long.valueOf(ADDRESS_ID));
        HttpEntity<Object> entity = getEntity(orderAddMockerRequest, getHeader());
        return httpCall("/users/" + USER_ID + "/orders", HttpMethod.POST, entity, ApiResponse.class);
    }

    @Test
    public void testCreateOrder_Success() {
        ResponseEntity<ApiResponse> response = addOrder();
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    public void testCreateOrder_Failure_InvalidUser() {
        CreateOrderRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(Long.valueOf(CART_ID));
        orderAddMockerRequest.setShippingAddressId(Long.valueOf(ADDRESS_ID));

        HttpEntity<Object> entity = getEntity(orderAddMockerRequest, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + "-1" + "/orders", HttpMethod.POST, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
    }

    @Test
    public void testCreateOrder_Failure_InvalidCart() {
        CreateOrderRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(-1L);
        orderAddMockerRequest.setShippingAddressId(Long.valueOf(ADDRESS_ID));

        HttpEntity<Object> entity = getEntity(orderAddMockerRequest, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders", HttpMethod.POST, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Invalid cart id!!", response.getBody().getMessage());
    }

    @Test
    public void testGetAllOrder_Success() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetAllOrder_Failure_InvalidUser() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + "-1" + "/orders", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetOrderId_Success() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders/" + ORDER_ID, HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetOrderId_Failure_InvalidUser() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + "-1" + "/orders/" + ORDER_ID, HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testGetOrderId_Failure_OrderNotFound() {
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders/-1", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Order not found", response.getBody().getMessage());
    }

    @Test
    public void testCancelOrder_Success() {
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders/" + ORDER_ID + "/cancel", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testCancelOrder_Failure_InvalidUser() {
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/-1/orders/" + ORDER_ID + "/cancel", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void ttestCancelOrder_Failure_OrderNotFound() {
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders/-1/cancel", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Order not found", response.getBody().getMessage());
    }

    @Test
    public void testCancelOrder_Failure_AlreadyUpdated() {
        HttpEntity<Object> entity = getEntity(getHeader());
        httpCall("/users/" + USER_ID + "/orders/" + ORDER_ID + "/cancel", HttpMethod.PUT, entity, ApiResponse.class);
        ResponseEntity<ApiResponse> response = httpCall("/users/" + USER_ID + "/orders/" + ORDER_ID + "/cancel", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("This order is already cancelled", response.getBody().getMessage());
    }
}