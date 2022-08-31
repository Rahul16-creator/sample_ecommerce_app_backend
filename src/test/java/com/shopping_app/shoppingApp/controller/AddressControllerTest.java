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
        return httpCall("/user/" + user.get("id") + "/address", HttpMethod.POST, entity, ApiResponse.class);
    }

    @Test
    public void testAddAddress() {
        ResponseEntity<ApiResponse> response = addAddress();
        assertEquals("Address added Successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());

        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/user/100/address", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());
    }

    @Test
    public void testUpdateAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<AddressRequest> entity = getEntity(MockPayload.getAddressUpdateRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id") + "/address/" + addressResponseData.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals("Address updated Successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/100/address/" + addressResponseData.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + user.get("id") + "/address/100", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response3.getStatusCodeValue());
        assertEquals("Address with this Id Not Found for this user!!", response3.getBody().getMessage());
    }

    @Test
    public void testDeleteAddress() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        ResponseEntity<ApiResponse> addressResponse = addAddress();
        Map<String, Object> addressResponseData = getResponseObjectData(addressResponse);
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id") + "/address/" + addressResponseData.get("id"), HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/100/address/" + addressResponseData.get("id"), HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());

        ResponseEntity<ApiResponse> response3 = httpCall("/user/" + user.get("id") + "/address/100", HttpMethod.DELETE, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response3.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());
    }
}