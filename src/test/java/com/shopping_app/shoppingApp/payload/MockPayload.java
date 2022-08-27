package com.shopping_app.shoppingApp.payload;

import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.Response.UserLoginResponse;

public class MockPayload {
    public static AddressRequest getAddressRequestPayload() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setState("testState");
        addressRequest.setCity("testCity");
        addressRequest.setStreet("testStreet");
        addressRequest.setCountry("testCountry");
        addressRequest.setPincode("123445");
        return addressRequest;
    }

    public static AddressRequest getAddressUpdateRequestPayload() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setState("testState");
        addressRequest.setCity("testCity");
        addressRequest.setStreet("street");
        addressRequest.setCountry("testCountry");
        addressRequest.setPincode("123445");
        return addressRequest;
    }


    public static Address getAddressMockData() {
        Address addressRequest = new Address();
        addressRequest.setState("testState");
        addressRequest.setCity("testCity");
        addressRequest.setStreet("testStreet");
        addressRequest.setCountry("testCountry");
        addressRequest.setPincode("123445");
        return addressRequest;
    }

    public static User getUserMockdata() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPhoneNumber("9876543211");
        return user;
    }

    public static UserRegisterRequest getUserRegisterMockRequestPayload() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("test");
        request.setEmail("test@gmail.com");
        request.setPassword("12345");
        request.setPhoneNumber("9876543211");
        return request;
    }

    public static UserLoginRequest getUserLoginMockRequestPayload() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("12345");
        return request;
    }

    public static UserLoginResponse getUserLoginMockResponsePayload() {
        UserLoginResponse userResponse = new UserLoginResponse();
        userResponse.setEmail("test@gmail.com");
        userResponse.setToken("9876543211");
        return userResponse;
    }

    public static UserUpdateRequest getUserUpdateMockRequestPayload() {
        return UserUpdateRequest.builder().name("test").email("test@gmail.com").phoneNumber("123456").build();
    }
}