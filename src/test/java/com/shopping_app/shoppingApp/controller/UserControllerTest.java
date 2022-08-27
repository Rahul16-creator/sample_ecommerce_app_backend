package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
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
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractController {

    @Test
    public void testUserRegister() {
        ResponseEntity<ApiResponse> httpResponse = addUser();
        assertEquals(200, httpResponse.getStatusCodeValue());
        ResponseEntity<ApiResponse> httpResponse2 = addUser();
        assertEquals(400, httpResponse2.getStatusCodeValue());
        assertEquals("User with this email Already exist", Objects.requireNonNull(httpResponse2.getBody()).getMessage());
    }

    @Test
    public void testUserLoginSuccess() {
        addUser();
        UserLoginRequest request = MockPayload.getUserLoginMockRequestPayload();
        HttpEntity<UserLoginRequest> entity = getEntity(request);
        ResponseEntity<ApiResponse> response = httpCall("/user/login", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        Object data = response.getBody().getData();
        assertNotNull(data);
    }

    @Test
    public void testUserById() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = addUser();
        Map<String, Object> user = (Map<String, Object>) apiResponseResponseEntity.getBody().getData();
        HttpHeaders httpHeaders = getHeader();
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        ResponseEntity<ApiResponse> response2 = httpCall("/user/100", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void testUpdateUser() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = addUser();
        Map<String, Object> user = (Map<String, Object>) apiResponseResponseEntity.getBody().getData();
        HttpEntity<UserUpdateRequest> entity = getEntity(MockPayload.getUserUpdateMockRequestPayload(), getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/user/" + user.get("id"), HttpMethod.PUT, entity, ApiResponse.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        ResponseEntity<ApiResponse> response2 = httpCall("/user/100", HttpMethod.PUT, entity, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }
}