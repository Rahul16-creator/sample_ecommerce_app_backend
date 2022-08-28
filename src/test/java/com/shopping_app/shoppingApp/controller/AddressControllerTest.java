package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerTest extends AbstractController {

    @Test
    public void testAddAddress() {
        addUser();
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/address/", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals("Address added Successfully", response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateAddress() {
        addUser();
        HttpEntity<AddressRequest> addressEntity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response1 = httpCall("/address/", HttpMethod.POST, addressEntity, ApiResponse.class);
        Map<String, Object> address = (Map<String, Object>) response1.getBody().getData();
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressUpdateRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/address/" + address.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals("Address updated Successfully", response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteAddress() {
        addUser();
        HttpEntity<AddressRequest> addressEntity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response1 = httpCall("/address/", HttpMethod.POST, addressEntity, ApiResponse.class);
        Map<String, Object> address = (Map<String, Object>) response1.getBody().getData();
        HttpHeaders httpHeaders = getHeader();
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ApiResponse> response = httpCall("/address/" + address.get("id"), HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals("Address deleted Successfully", response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }
}