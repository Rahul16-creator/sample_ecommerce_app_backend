package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.User.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.User.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.UserResponse;
import com.shopping_app.shoppingApp.service.UserService;
import com.shopping_app.shoppingApp.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserController {

    private final UserService userService;

    // testing purpose  added this api..
    @GetMapping
    public ResponseEntity<ApiResponse> fetchAllUser() {
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(ResponseUtil.createResponse("All Users Fetched Successfully", users, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse user = userService.registerUser(userRegisterRequest);
        return new ResponseEntity<>(ResponseUtil.createResponse("Users Registered Successfully!!", user, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse user = userService.loginUser(userLoginRequest);
        return new ResponseEntity<>(ResponseUtil.createResponse("Users Logged In Successfully!!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserFromId(userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("User Fetched Successfully", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> updateUserById(@Valid @RequestBody UserUpdateRequest userProfileUpdateRequest, @PathVariable Long userId) {
        UserResponse user = userService.updateUserById(userProfileUpdateRequest, userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Users Profile Updated Successfully!!", user, HttpStatus.OK), HttpStatus.OK);
    }

}