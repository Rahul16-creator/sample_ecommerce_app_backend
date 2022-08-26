package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.Response.UserResponse;
import com.shopping_app.shoppingApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserController {

    private final UserService userService;

    // testing purpose  added this api..
    @GetMapping("/")
    public ResponseEntity<ApiResponse> fetchAllUser() {
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(getResponse("All Users Fetched Successfully", users.size(), users), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse user = userService.registerUser(userRegisterRequest);
        return new ResponseEntity<>(getResponse("Users Registered Successfully!!", 1, user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse user = userService.loginUser(userLoginRequest);
        return new ResponseEntity<>(getResponse("Users Logged In Successfully!!", 1, user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return new ResponseEntity<>(getResponse("User Fetched Successfully", 1, user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUserById(@RequestBody UserUpdateRequest userProfileUpdateRequest, @PathVariable Long id) {
        UserResponse user = userService.updateUserById(userProfileUpdateRequest, id);
        return new ResponseEntity<>(getResponse("Users Profile Updated Successfully!!", 1, user), HttpStatus.OK);
    }

    public ApiResponse getResponse(String message, Integer count, Object data) {
        return ApiResponse.builder()
                .data(data)
                .code(HttpStatus.OK)
                .message(message)
                .count(count)
                .status(ResponseType.SUCCESS)
                .build();
    }
}
