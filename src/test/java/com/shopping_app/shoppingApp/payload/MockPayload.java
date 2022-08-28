package com.shopping_app.shoppingApp.payload;

import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.mapping.ProductMapperImpl;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartItemRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.Response.UserLoginResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static ProductRequest getProductRequestMockPayload() {
        return ProductRequest.builder()
                .productName("test_product")
                .availableQuantity(1)
                .price(10.1F)
                .description("test description")
                .build();
    }

    public static ProductResponse getProductResponseMockPayload() {
        return ProductResponse.builder()
                .id(1L)
                .productName("test_product")
                .availableQuantity(1)
                .productPrice(10.1F)
                .productDescription("test description")
                .build();
    }

    public static Product getProductMockPayload() {
        return Product.builder()
                .productName("test_product")
                .availableQuantity(1)
                .price(10.1F)
                .description("test description")
                .build();
    }

    public static List<Product> getAllProductsMockData() {
        ProductMapper productMapper = new ProductMapperImpl();
        ProductRequest request1 = ProductRequest.builder().productName("test_product1").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request2 = ProductRequest.builder().productName("test_product2").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request3 = ProductRequest.builder().productName("test_product3").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request4 = ProductRequest.builder().productName("test_product4").availableQuantity(1).price(10.1F).description("test description").build();
        ProductRequest request5 = ProductRequest.builder().productName("test_product5").availableQuantity(1).price(10.1F).description("test description").build();
        Product product1 = productMapper.convertToProductDomain(request1);
        Product product2 = productMapper.convertToProductDomain(request2);
        Product product3 = productMapper.convertToProductDomain(request3);
        Product product4 = productMapper.convertToProductDomain(request4);
        Product product5 = productMapper.convertToProductDomain(request5);
        return Arrays.asList(product1, product2, product3, product4, product5);
    }

    public static CartAddRequest getCartAddRequestPayload() {
        CartItemRequest cartItem = CartItemRequest.builder().product_id(1L).quantity(1).build();
        return CartAddRequest.builder().cartItemRequest(cartItem).build();
    }

    public static Cart getCartMockData() {
        Cart cart = new Cart();
        cart.setUser(getUserMockdata());
        CartItems item = CartItems.builder().quantity(1).product(getProductMockPayload()).build();
        Set<CartItems> cartItems = new HashSet<CartItems>();
        cartItems.add(item);
        cart.setCartItems(cartItems);
        return cart;
    }

    public static CartItems getCartItemMockData() {
        return CartItems.builder().quantity(1).product(getProductMockPayload()).build();
    }

    public static CartItemUpdateRequest getCartItemUpdateRequestPayload() {
        return CartItemUpdateRequest.builder().quantity(1).id(1L).build();
    }

}