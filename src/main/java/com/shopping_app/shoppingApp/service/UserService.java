package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.model.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.Response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);

    UserLoginResponse loginUser(UserLoginRequest userLoginRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUserById(UserUpdateRequest userProfileUpdateRequest, Long id);
}