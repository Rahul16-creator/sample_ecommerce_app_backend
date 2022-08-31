package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.model.User.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.UserResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUserRegisterSuccess() {
        UserRegisterRequest userRegisterRequest = MockPayload.getUserRegisterMockRequestPayload();
        userRegisterRequest.setEmail("xyz@gmail.com");
        UserResponse userResponse = userService.registerUser(userRegisterRequest);
        assertEquals(userRegisterRequest.getEmail(), userResponse.getEmail());
    }

    @Test
    public void testUserRegisterFailure() {
        UserRegisterRequest userRegisterRequest = MockPayload.getUserRegisterMockRequestPayload();
        try {
            userService.registerUser(userRegisterRequest);
        } catch (UserAlreadyExist ex) {
            assertEquals("User with this email Already exist", ex.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        }
    }

    @Test
    public void testUserLoginSuccess() {
        UserLoginRequest userLoginRequest = MockPayload.getUserLoginMockRequestPayload();
        UserLoginResponse userLoginResponse = userService.loginUser(userLoginRequest);
        assertNotNull(userLoginResponse.getToken());
        assertEquals(userLoginRequest.getEmail(), userLoginResponse.getEmail());
    }

    @Test
    public void testUserLoginFailure_NOT_FOUND() {
        UserLoginRequest userLoginRequest = MockPayload.getUserLoginMockRequestPayload();
        userLoginRequest.setEmail("xyz@gmail.com");
        try {
            userService.loginUser(userLoginRequest);
        } catch (Exception ex) {
            assertEquals("User With this email not exist", ex.getMessage());
        }
    }

    @Test
    public void testUserLoginFailure_WRONG_CREDENTIALS() {
        UserLoginRequest userLoginRequest = MockPayload.getUserLoginMockRequestPayload();
        userLoginRequest.setPassword("12345");
        try {
            userService.loginUser(userLoginRequest);
        } catch (Exception ex) {
            assertEquals("Bad credentials", ex.getMessage());
        }
    }

    @Test
    public void testUserGetUserById() {
        UserResponse userResponse = userService.getUserFromId(userId);
        assertNotNull(userResponse);
        assertEquals(MockPayload.getUserMockdata().getEmail(), userResponse.getEmail());
    }

    @Test
    public void testUserUpdate() {
        UserResponse userResponse = userService.updateUserById(MockPayload.getUserUpdateMockRequestPayload(), userId);
        assertNotNull(userResponse);
        assertEquals(MockPayload.getUserUpdateMockRequestPayload().getPhoneNumber(), userResponse.getPhoneNumber());
    }
}