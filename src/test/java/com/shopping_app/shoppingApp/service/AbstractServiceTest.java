package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.payload.MockPayload;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AbstractServiceTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CartRepository cartRepository;

    public Long userId;

    @Before
    public void SetUp() {
        cleanUp();
        User user = MockPayload.getUserMockdata();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userId = savedUser.getId();
    }

    @After
    public void cleanUp() {
        cartRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }
}
