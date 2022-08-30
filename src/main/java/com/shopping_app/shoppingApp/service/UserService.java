package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.Exceptions.UserAlreadyExist;
import com.shopping_app.shoppingApp.config.JWT.JwtTokenProvider;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.model.Enum.UserRole;
import com.shopping_app.shoppingApp.model.User.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.User.Request.UserUpdateRequest;
import com.shopping_app.shoppingApp.model.User.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.User.Response.UserResponse;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.UserRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        Optional<User> isUserExist = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (isUserExist.isPresent()) {
            throw new UserAlreadyExist("User with this email Already exist", HttpStatus.BAD_REQUEST);
        }
        User user = convertToUserDomain(userRegisterRequest);
        Set<Address> address = user.getAddress();
        user.setAddress(null);
        user.setRoleName(UserRole.CUSTOMER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        setAddressToUser(address, savedUser);
        // After registration create cart for users
        createEmptyCart(savedUser);
        savedUser.setAddress(address);
        return convertToUserResponse(savedUser);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(userLoginRequest.getEmail());
        return convertToUserLoginResponse(userLoginRequest, token);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return convertToUserDomainList(users);
    }

    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User Not Found !", HttpStatus.NOT_FOUND);
        }
        return convertToUserResponse(user.get());
    }

    public UserResponse updateUserById(UserUpdateRequest UserProfileUpdateRequest, Long id) {
        User user = fetchUserById(id);
        user.setName(UserProfileUpdateRequest.getName());
        user.setName(UserProfileUpdateRequest.getName());
        user.setName(UserProfileUpdateRequest.getEmail());
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
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

    public List<UserResponse> convertToUserDomainList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( users.size() );
        for ( User user : users ) {
            list.add( convertToUserResponse( user ) );
        }

        return list;
    }

    public User convertToUserDomain(UserRegisterRequest userRegisterRequest) {
        if ( userRegisterRequest == null ) {
            return null;
        }

        User user = new User();

        user.setName( userRegisterRequest.getName() );
        user.setEmail( userRegisterRequest.getEmail() );
        user.setPhoneNumber( userRegisterRequest.getPhoneNumber() );
        user.setPassword( userRegisterRequest.getPassword() );
        Set<Address> set = userRegisterRequest.getAddress();
        if ( set != null ) {
            user.setAddress( new LinkedHashSet<Address>( set ) );
        }

        return user;
    }

    public UserResponse convertToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setName( user.getName() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setPhoneNumber( user.getPhoneNumber() );
        userResponse.setAddress( addressSetToAddressResponseSet( user.getAddress() ) );

        return userResponse;
    }

    public UserLoginResponse convertToUserLoginResponse(UserLoginRequest user, String token) {
        if ( user == null && token == null ) {
            return null;
        }

        UserLoginResponse.UserLoginResponseBuilder userLoginResponse = UserLoginResponse.builder();

        if ( user != null ) {
            userLoginResponse.email( user.getEmail() );
        }
        userLoginResponse.token( token );

        return userLoginResponse.build();
    }

    public AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse addressResponse = new AddressResponse();

        addressResponse.setId( address.getId() );
        addressResponse.setStreet( address.getStreet() );
        addressResponse.setCity( address.getCity() );
        addressResponse.setState( address.getState() );
        addressResponse.setPincode( address.getPincode() );
        addressResponse.setCountry( address.getCountry() );

        return addressResponse;
    }

    public Set<AddressResponse> addressSetToAddressResponseSet(Set<Address> set) {
        if ( set == null ) {
            return null;
        }

        Set<AddressResponse> set1 = new LinkedHashSet<AddressResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Address address : set ) {
            set1.add( addressToAddressResponse( address ) );
        }

        return set1;
    }
}