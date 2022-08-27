package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.OrderRepository;
import com.shopping_app.shoppingApp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.shopping_app.shoppingApp.payload.MockPayload.getUserRegisterMockRequestPayload;

public class AbstractController {

    public static final String AUTHORIZATION = "Authorization";

    public static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY2MTU3ODY0NywiZXhwIjoxNjYxNjY1MDQ3fQ.gSNsoRwOnZ4bDsIUb2Fx3MzbSWRj0JOu-z0gjbO6LAY";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Before
    public void clearUsersDBData() {
        clearDB();
    }

    public ResponseEntity<ApiResponse> addUser() {
        UserRegisterRequest userMockRequestPayload = getUserRegisterMockRequestPayload();
        HttpEntity<UserRegisterRequest> entity = getEntity(userMockRequestPayload);
        return httpCall("/user/register", HttpMethod.POST, entity, ApiResponse.class);
    }

    public <T> HttpEntity<T> getEntity(T data) {
        return new HttpEntity<>(data);
    }

    public <T> HttpEntity<T> getEntity(T data, HttpHeaders httpHeader) {
        return new HttpEntity<>(data, httpHeader);
    }

    public <T, E> ResponseEntity<T> httpCall(String url, HttpMethod httpMethod, HttpEntity<E> entity, Class<T> responseType) {
        return restTemplate.exchange(url, httpMethod, entity, responseType);
    }

    public HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, AUTH_TOKEN);
        return httpHeaders;
    }

    @After
    public void clearDB() {
        cartRepository.deleteAll();
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }
}
