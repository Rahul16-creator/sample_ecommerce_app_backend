package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.config.JWT.JwtTokenProvider;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.UserMapper;
import com.shopping_app.shoppingApp.model.Enum.UserRole;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.Response.UserResponse;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        Optional<User> isUserExist = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (isUserExist.isPresent()) {
            throw new UserAlreadyExist("User with this email Already exist", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.convertToUserDomain(userRegisterRequest);
        Set<Address> address = user.getAddress();
        user.setAddress(null);
        user.setRoleName(UserRole.CUSTOMER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        setAddressToUser(address, savedUser);
        // After registration create cart for users
        createEmptyCart(savedUser);
        savedUser.setAddress(address);
        return userMapper.convertToUserResponse(savedUser);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(userLoginRequest.getEmail());
        return userMapper.convertToUserLoginResponse(userLoginRequest, token);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.convertToUserDomainList(users);
    }

    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User Not Found !", HttpStatus.NOT_FOUND);
        }
        return userMapper.convertToUserResponse(user.get());
    }

    public UserResponse updateUserById(UserUpdateRequest UserProfileUpdateRequest, Long id) {
        User user = fetchUserById(id);
        user.setName(UserProfileUpdateRequest.getName());
        user.setName(UserProfileUpdateRequest.getName());
        user.setName(UserProfileUpdateRequest.getEmail());
        User updatedUser = userRepository.save(user);
        return userMapper.convertToUserResponse(updatedUser);
    }

    public User fetchUserById(Long id) {
        Optional<User> isUserExist = userRepository.findById(id);
        if (isUserExist.isEmpty()) {
            throw new NotFoundException("User with this Id Not Found ", HttpStatus.NOT_FOUND);
        }
        return isUserExist.get();
    }

    public void setAddressToUser(Set<Address> address, User user) {
        if (null != address && address.size() > 0) {
            for (Address e : address) {
                e.setUser(user);
            }
            addressRepository.saveAll(address);
        }
    }

    public void createEmptyCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }
}