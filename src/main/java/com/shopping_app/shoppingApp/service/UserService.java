package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.model.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);
    UserResponse LoginUser(UserLoginRequest userLoginRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUserById(UserRegisterRequest userRegisterRequest,Long id);

}
