package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerTest extends AbstractControllerTest {

    public ResponseEntity<ApiResponse> addAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        return httpCall("/users/" + user.get("id") + "/addresses", HttpMethod.POST, entity, ApiResponse.class);
    }

    @Test
    public void testAddAddress_Success() {
        ResponseEntity<ApiResponse> response = addAddress();
        assertEquals("Address added Successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    public void testAddAddress_Failure_InvalidUser() {
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/100/addresses", HttpMethod.POST, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testUpdateAddress_Success() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressUpdateRequestPayload(), getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + user.get("id") + "/addresses/" + addressResponseData.get("id"), HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals("Address updated Successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testUpdateAddress_Failure_InvalidUser() {
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressUpdateRequestPayload(), getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/100/addresses/" + addressResponseData.get("id"), HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testUpdateAddress_Failure_InvalidAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressUpdateRequestPayload(), getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + user.get("id") + "/addresses/100", HttpMethod.PUT, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Address with this Id Not Found for this user!!", response.getBody().getMessage());
    }

    @Test
    public void testDeleteAddress_Success() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<Object> entity = getEntity(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + user.get("id") + "/addresses/" + addressResponseData.get("id"), HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteAddress_Failure_InvalidUser() {
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<Object> entity = getEntity(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/100/addresses/" + addressResponseData.get("id"), HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }

    @Test
    public void testDeleteAddress_Failure_InvalidAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<Object> entity = getEntity(getHeader());

        ResponseEntity<ApiResponse> response = httpCall("/users/" + user.get("id") + "/addresses/100", HttpMethod.DELETE, entity, ApiResponse.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Address with this Id Not Found for this user!!", response.getBody().getMessage());
    }
}