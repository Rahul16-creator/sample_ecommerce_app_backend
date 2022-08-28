package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import com.shopping_app.shoppingApp.model.Order.Request.OrderAddRequest;
import com.shopping_app.shoppingApp.model.Order.Request.OrderUpdateRequest;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderControllerTest extends AbstractController {

    @Test
    public void testAddOrder() {
        OrderAddRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        ResponseEntity<ApiResponse> response = addOrder();
        assertEquals(200, response.getStatusCodeValue());
        orderAddMockerRequest.setCartId(1L);
        orderAddMockerRequest.setShippingAddressId(100L);
        HttpEntity<Object> entity2 = getEntity(orderAddMockerRequest, getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/order/", HttpMethod.POST, entity2, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void testGetAllOrder() {
        addUser();
        addAddress();
        addItemInCart();
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/order/", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrderId() {
        ResponseEntity<ApiResponse> orderResponse = addOrder();
        Map<String, Object> order = (Map<String, Object>) orderResponse.getBody().getData();
        HttpEntity<Object> entity = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/order/" + order.get("id"), HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        ResponseEntity<ApiResponse> response2 = httpCall("/order/100", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void testUpdateOrder() {
        ResponseEntity<ApiResponse> orderResponse = addOrder();
        Map<String, Object> order = (Map<String, Object>) orderResponse.getBody().getData();
        OrderUpdateRequest orderUpdateRequest = MockPayload.getOrderUpdateRequestMockPayload();
        orderUpdateRequest.setId(Long.valueOf(String.valueOf(order.get("id"))));
        orderUpdateRequest.setOrderStatus(OrderStatus.DELIVERED);
        HttpEntity<Object> entity = getEntity(orderUpdateRequest, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/order/", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        orderUpdateRequest.setId(100L);
        HttpEntity<Object> entity2 = getEntity(orderUpdateRequest, getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/order/", HttpMethod.PUT, entity2, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }


    public ResponseEntity<ApiResponse> addOrder() {
        addUser();
        String addressId = addAddress();
        String cartId = addItemInCart();
        OrderAddRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(Long.valueOf(cartId));
        orderAddMockerRequest.setShippingAddressId(Long.valueOf(addressId));
        HttpEntity<Object> entity = getEntity(orderAddMockerRequest, getHeader());
        return httpCall("/order/", HttpMethod.POST, entity, ApiResponse.class);
    }

    public String addItemInCart() {
        String id = addMockProduct();
        CartAddRequest request = MockPayload.getCartAddRequestPayload();
        request.getCartItemRequest().setProduct_id(Long.valueOf(id));
        HttpEntity<CartAddRequest> entity = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> cartResponse = httpCall("/cart/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> cart = (Map<String, Object>) cartResponse.getBody().getData();
        return String.valueOf(cart.get("id"));
    }

    public String addMockProduct() {
        ProductRequest productRequest = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(productRequest, getHeader());
        ResponseEntity<ApiResponse> productResponse = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> product = (Map<String, Object>) productResponse.getBody().getData();
        return String.valueOf(product.get("id"));
    }

    public String addAddress() {
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/address/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> address = (Map<String, Object>) response.getBody().getData();
        return String.valueOf(address.get("id"));
    }

}
