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
    public void testUserLogin_Success() {
        UserLoginRequest request = MockPayload.getUserLoginMockRequestPayload();
        HttpEntity<UserLoginRequest> entity = getEntity(request);
        ResponseEntity<ApiResponse> response = httpCall("/user/login", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Object data1 = response.getBody().getData();
        assertNotNull(data1);
    }

    @Test
    public void testUserLogin_Failure_InvalidUser() {
        UserLoginRequest request = MockPayload.getUserLoginMockRequestPayload();
        request.setEmail("xyz@gmail.com");
        HttpEntity<UserLoginRequest> entity = getEntity(request);
        ResponseEntity<ApiResponse> response = httpCall("/user/login", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        assertEquals("User With this email not exist", response.getBody().getMessage());
    }

    @Test
    public void testUserLogin_Failure_InvalidCredentials() {
        UserLoginRequest request = MockPayload.getUserLoginMockRequestPayload();
        request.setPassword("xyz");
        HttpEntity<UserLoginRequest> entity = getEntity(request);
        ResponseEntity<ApiResponse> response = httpCall("/user/login", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        assertEquals("Bad credentials", response.getBody().getMessage());
    }

    @Test
    public void testUserById_Success() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testUserById_Failure_InvalidUser() {
        HttpEntity<Object> entity = getEntity(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/100", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }


    @Test
    public void testUpdateUser_Success() {
        Map<String, Object> user = getResponseObjectData(USER_RESPONSE_ENTITY);
        HttpEntity<UserUpdateRequest> entity = getEntity(MockPayload.getUserUpdateMockRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUser_Failure_InvalidUser() {
        HttpEntity<UserUpdateRequest> entity = getEntity(MockPayload.getUserUpdateMockRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/100", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
        assertEquals("Access is denied", response.getBody().getMessage());
    }
}