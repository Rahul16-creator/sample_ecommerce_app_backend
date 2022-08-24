package com.shopping_app.shoppingApp.service.impl;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.UserMapper;
import com.shopping_app.shoppingApp.model.Enum.UserRole;
import com.shopping_app.shoppingApp.model.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Response.UserResponse;
import com.shopping_app.shoppingApp.repository.UserRepository;
import com.shopping_app.shoppingApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        Optional<User> isUserExist = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (isUserExist.isPresent()) {
            throw new UserAlreadyExist("User with this email Already exist ", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.convertToUserDomain(userRegisterRequest);
        user.setRoleName(UserRole.CUSTOMER);
        User saveUser = userRepository.save(user);
        return userMapper.convertToUserResponse(saveUser);
    }

    @Override
    public UserResponse LoginUser(UserLoginRequest userLoginRequest) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::convertToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("User Not Found !", HttpStatus.NOT_FOUND);
        }
        return userMapper.convertToUserResponse(user.get());
    }

    @Override
    public UserResponse updateUserById(UserRegisterRequest userRegisterRequest, Long id) {
        User user = fetchUserById(id);
        if (StringUtils.isNotBlank(userRegisterRequest.getName())) {
            user.setName(userRegisterRequest.getName());
        }
        if (StringUtils.isNotBlank(userRegisterRequest.getPhoneNumber())) {
            user.setName(userRegisterRequest.getName());
        }
        if (StringUtils.isNotBlank(userRegisterRequest.getEmail())) {
            user.setName(userRegisterRequest.getEmail());
        }
        return userMapper.convertToUserResponse(userRepository.save(user));
    }

    public User fetchUserById(Long id) {
        Optional<User> isUserExist = userRepository.findById(id);
        if (isUserExist.isEmpty()) {
            throw new NotFoundException("User with this Id Not Found ", HttpStatus.NOT_FOUND);
        }
        return isUserExist.get();
    }

}
