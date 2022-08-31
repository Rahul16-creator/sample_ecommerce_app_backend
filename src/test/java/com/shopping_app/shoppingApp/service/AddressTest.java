package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AddressTest extends AbstractServiceTest {

    @Autowired
    private AddressService addressService;

    private Long addressId;

    @Before
    public void setUp() {
        Address address = MockPayload.getAddressMockData();
        address.setUser(userRepository.findById(userId).get());
        Address saveAddress = addressRepository.save(address);
        addressId = saveAddress.getId();
    }

    @Test
    public void testAddAddress() {
        AddressRequest addressRequest = MockPayload.getAddressRequestPayload();
        AddressResponse addressResponse = addressService.addAddress(addressRequest, userId);
        assertNotNull(addressResponse);
        assertEquals(addressRequest.getCity(), addressResponse.getCity());
    }

    @Test
    public void testAddressUpdateSuccess() {
        AddressRequest addressRequest = MockPayload.getAddressUpdateRequestPayload();
        AddressResponse addressResponse = addressService.updateAddress(addressRequest, userId, addressId);
        assertNotNull(addressResponse);
        assertEquals(addressRequest.getStreet(), addressResponse.getStreet());
    }

    @Test
    public void testAddressUpdateFailure() {
        AddressRequest addressRequest = MockPayload.getAddressUpdateRequestPayload();
        try {
            addressService.updateAddress(addressRequest, userId, -100L);
        } catch (BaseException ex) {
            assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
            assertEquals("Address with this Id Not Found for this user!!", ex.getMessage());
        }
    }


    @Test
    public void testDeleteAddressFailure() {
        try {
            addressService.deleteAddress(userId, -100L);
        } catch (BaseException ex) {
            assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
            assertEquals("Address with this Id Not Found for this user!!", ex.getMessage());
        }
    }
}