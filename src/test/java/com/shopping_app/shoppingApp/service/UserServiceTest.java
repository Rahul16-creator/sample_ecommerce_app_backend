package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.config.JWT.JwtTokenProvider;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.UserMapper;
import com.shopping_app.shoppingApp.mapping.UserMapperImpl;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.Response.UserResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    private UserMapperImpl userMapperImpl = new UserMapperImpl();

    @Test
    public void testRegisterUserSuccess() {
        UserRegisterRequest request = MockPayload.getUserRegisterMockRequestPayload();
        User user = userMapperImpl.convertToUserDomain(request);
        UserResponse userResponse = userMapperImpl.convertToUserResponse(user);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(MockPayload.getUserMockdata());
        when(userMapper.convertToUserDomain(any(UserRegisterRequest.class))).thenReturn(user);
        when(userMapper.convertToUserResponse(any(User.class))).thenReturn(userResponse);
        UserResponse response = userService.registerUser(request);
        assertNotNull(response);
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserFailure() {
        UserRegisterRequest request = MockPayload.getUserRegisterMockRequestPayload();
        when(userRepository.findByEmail(any())).thenThrow(UserAlreadyExist.class);
        assertThrows(UserAlreadyExist.class, () -> userService.registerUser(request));
    }

    //    TODO check for solution
//    @Test
    public void testUserLoginSuccess() {
        String token = "7423672436737623";
        UserLoginRequest userLoginMockRequestPayload = MockPayload.getUserLoginMockRequestPayload();
        UserLoginResponse response = userMapperImpl.convertToUserLoginResponse(userLoginMockRequestPayload, token);
        when(authenticationManager.authenticate(any())).thenReturn(any());
        when(tokenProvider.createToken(any())).thenReturn(token);
        when(userMapper.convertToUserLoginResponse(any(UserLoginRequest.class), eq(token))).thenReturn(response);
        UserLoginResponse userLoginResponse = userService.loginUser(userLoginMockRequestPayload);
        assertNotNull(userLoginResponse.getToken());
    }

    @Test
    public void testUserFindByIdFailure() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testUserFindByIdSuccess() {
        User userMockdata = MockPayload.getUserMockdata();
        UserResponse user = userMapperImpl.convertToUserResponse(userMockdata);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMockdata));
        when(userMapper.convertToUserResponse(any())).thenReturn(user);
        UserResponse userById = userService.getUserById(1L);
        assertEquals(userById.getEmail(), "test@gmail.com");
    }

    @Test
    public void testUserUpdateProfileSuccess() {
        User userMockdata = MockPayload.getUserMockdata();
        UserResponse user = userMapperImpl.convertToUserResponse(userMockdata);
        UserUpdateRequest userUpdateRequest = MockPayload.getUserUpdateMockRequestPayload();
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMockdata));
        when(userRepository.save(any())).thenReturn(MockPayload.getUserMockdata());
        when(userMapper.convertToUserResponse(any())).thenReturn(user);
        UserResponse userResponse = userService.updateUserById(userUpdateRequest, 1L);
        assertNotNull(userResponse);
    }

    @Test
    public void testUserUpdateProfileFailure() {
        UserUpdateRequest userUpdateRequest = MockPayload.getUserUpdateMockRequestPayload();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUserById(userUpdateRequest, -1L));
    }

}