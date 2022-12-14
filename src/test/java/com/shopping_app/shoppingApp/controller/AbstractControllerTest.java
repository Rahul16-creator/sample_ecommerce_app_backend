package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.User.UserRegisterRequest;
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

import java.util.Map;

import static com.shopping_app.shoppingApp.payload.MockPayload.getUserRegisterMockRequestPayload;

public class AbstractControllerTest {

    public static final String AUTHORIZATION = "Authorization";

    public static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY2MjM4NjY4OCwiZXhwIjoxNjYyNDczMDg4fQ.U3WWXUsaRVr7pyH0v6zTLukvxgvP8cKZYR6rfk4nd2A";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    public CartRepository cartRepository;

    @Autowired
    public OrderRepository orderRepository;

    public ResponseEntity<ApiResponse> USER_RESPONSE_ENTITY;


    @Before
    public void clearUsersDBData() {
        cleanUp();
        USER_RESPONSE_ENTITY = addUser();
    }

    public ResponseEntity<ApiResponse> addUser() {
        UserRegisterRequest userMockRequestPayload = getUserRegisterMockRequestPayload();
        HttpEntity<UserRegisterRequest> entity = getEntity(userMockRequestPayload);
        return httpCall("/user/register", HttpMethod.POST, entity, ApiResponse.class);
    }

    public Map<String, Object> getResponseObjectData(ResponseEntity<ApiResponse> apiResponseResponseEntity) {
        return (Map<String, Object>) apiResponseResponseEntity.getBody().getData();
    }

    public <T> HttpEntity<T> getEntity(T data) {
        return new HttpEntity<>(data);
    }

    public <T> HttpEntity<T> getEntity(T data, HttpHeaders httpHeader) {
        return new HttpEntity<>(data, httpHeader);
    }

    public HttpEntity<Object> getEntity(HttpHeaders httpHeader) {
        return new HttpEntity<>(httpHeader);
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
    public void cleanUp() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }
}
