package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.User.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.UserUpdateRequest;
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
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void testUserRegister() {
        assertEquals(HttpStatus.CREATED.value(), USER_RESPONSE_ENTITY.getStatusCodeValue());
        ResponseEntity<ApiResponse> httpResponse2 = addUser();
        assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse2.getStatusCodeValue());
        assertEquals("User with this email Already exist", Objects.requireNonNull(httpResponse2.getBody()).getMessage());
    }

    @Test
    public void testUserLogin() {
        UserLoginRequest request1 = MockPayload.getUserLoginMockRequestPayload();
        HttpEntity<UserLoginRequest> entity1 = getEntity(request1);
        ResponseEntity<ApiResponse> response1 = httpCall("/user/login", HttpMethod.POST, entity1, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response1.getStatusCodeValue());
        Object data1 = response1.getBody().getData();
        assertNotNull(data1);

        UserLoginRequest request2 = MockPayload.getUserLoginMockRequestPayload();
        request2.setEmail("xyz@gmail.com");
        HttpEntity<UserLoginRequest> entity2 = getEntity(request2);
        ResponseEntity<ApiResponse> response2 = httpCall("/user/login", HttpMethod.POST, entity2, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response2.getStatusCodeValue());
        assertEquals("User With this email not exist", response2.getBody().getMessage());

        UserLoginRequest request3 = MockPayload.getUserLoginMockRequestPayload();
        request3.setPassword("xyz");
        HttpEntity<UserLoginRequest> entity3 = getEntity(request3);
        ResponseEntity<ApiResponse> response3 = httpCall("/user/login", HttpMethod.POST, entity3, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response3.getStatusCodeValue());
        assertEquals("Bad credentials", response3.getBody().getMessage());

    }

    @Test
    public void testUserById() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/100", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());
    }

    @Test
    public void testUpdateUser() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<UserUpdateRequest> entity = getEntity(MockPayload.getUserUpdateMockRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());

        ResponseEntity<ApiResponse> response2 = httpCall("/user/100", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response2.getStatusCodeValue());
        assertEquals("Access is denied", response2.getBody().getMessage());
    }
}