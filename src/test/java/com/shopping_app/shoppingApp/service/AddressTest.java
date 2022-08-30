package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AddressTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserPrincipal applicationUser;

    @Mock
    private Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    private AddressService addressService;


    @Test
    public void testAddAddress() {
        AddressRequest addressRequestPayload = MockPayload.getAddressRequestPayload();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(userService.fetchUserById(any(Long.class))).thenReturn(MockPayload.getUserMockdata());
        when(addressRepository.save(any(Address.class))).thenReturn(MockPayload.getAddressMockData());
        AddressResponse response1 = addressService.addAddress(addressRequestPayload);
        assertEquals(response1.getCity(), addressRequestPayload.getCity());
    }

    @Test
    public void testUpdateAddressSuccess() {
        AddressRequest request = MockPayload.getAddressUpdateRequestPayload();
        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(MockPayload.getAddressMockData()));
        when(addressRepository.save(any(Address.class))).thenReturn(MockPayload.getAddressMockData());
        AddressResponse response = addressService.updateAddress(request, 1L);
        assertNotNull(response);
    }

    @Test
    public void testUpdateAddressFailure() {
        AddressRequest addressRequestPayload = MockPayload.getAddressRequestPayload();
        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> addressService.updateAddress(addressRequestPayload, 1L));
    }

    @Test
    public void testAddressDeleteSuccess() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.ofNullable(MockPayload.getAddressMockData()));
        AddressResponse response = addressService.deleteAddress(1L);
        assertNotNull(response);
    }

    @Test
    public void testAddressDeleteFailure() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> addressService.deleteAddress(1L));
    }
}