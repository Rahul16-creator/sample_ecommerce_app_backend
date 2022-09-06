package com.shopping_app.shoppingApp.payload;

import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import com.shopping_app.shoppingApp.model.Enum.UserRole;
import com.shopping_app.shoppingApp.model.Order.OrderAddRequest;
import com.shopping_app.shoppingApp.model.Order.OrderUpdateRequest;
import com.shopping_app.shoppingApp.model.User.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.UserUpdateRequest;

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
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPassword("12345");
        user.setPhoneNumber("9876543211");
        user.setRoleName(UserRole.CUSTOMER);
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

    public static UserUpdateRequest getUserUpdateMockRequestPayload() {
        return UserUpdateRequest.builder().name("test").phoneNumber("123456").build();
    }

    public static CartAddRequest getCartAddRequestPayload() {
        return CartAddRequest.builder().product_id(1L).quantity(1).build();
    }

    public static CartItemUpdateRequest getCartItemUpdateRequestPayload() {
        return CartItemUpdateRequest.builder().quantity(2).build();
    }

    public static OrderAddRequest getOrderAddMockerRequest() {
        return OrderAddRequest.builder().cartId(1L).shippingAddressId(1L).build();
    }

    public static OrderUpdateRequest getOrderUpdateMockerRequest() {
        return OrderUpdateRequest.builder().orderStatus(OrderStatus.CANCELLED).build();
    }
}