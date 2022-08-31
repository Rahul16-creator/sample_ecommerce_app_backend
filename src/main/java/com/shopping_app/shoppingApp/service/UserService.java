package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.config.JWT.JwtTokenProvider;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Enum.UserRole;
import com.shopping_app.shoppingApp.model.User.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.UserResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExist("User with this email Already exist", HttpStatus.BAD_REQUEST);
        }
        User user = convertToUserDomain(userRegisterRequest);
        user.setRoleName(UserRole.CUSTOMER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if (null != userRegisterRequest.getAddress()) {
            Set<Address> address = userRegisterRequest.getAddress().stream().map(AddressService::convertToAddress).collect(Collectors.toSet());
            setAddressToUser(address, savedUser);
            savedUser.setAddress(address);
        }
        // After registration create cart for users
        createEmptyCart(savedUser);
        return UserResponse.from(savedUser);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(userLoginRequest.getEmail());
        return UserLoginResponse.from(userLoginRequest, token);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse getUserById(Long id) {
        return UserResponse.from(fetchUserById(id));
    }

    public UserResponse updateUserById(UserUpdateRequest userUpdateRequest, Long id) {
        User user = fetchUserById(id);
        user.setName(userUpdateRequest.getName());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        User updatedUser = userRepository.save(user);
        return UserResponse.from(updatedUser);
    }

    public User fetchUserById(Long id) {
        Optional<User> isUserExist = userRepository.findById(id);
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

    private void createEmptyCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    public User convertToUserDomain(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setName(userRegisterRequest.getName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());
        user.setPassword(userRegisterRequest.getPassword());
        return user;
    }
}