package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    public AddressResponse addAddress(AddressRequest addressRequest, Long userId) {
        Address address = convertToAddress(addressRequest);
        User user = userService.getUserById(userId);
        address.setUser(user);
        Address saveAddress = addressRepository.save(address);
        return AddressResponse.from(saveAddress);
    }

    public AddressResponse updateAddress(AddressRequest addressRequest, Long userId, Long addressId) {
        Address address = getAddressById(userId, addressId);
        address.setStreet(addressRequest.getStreet());
        address.setState(addressRequest.getState());
        address.setCountry(addressRequest.getCountry());
        address.setCity(addressRequest.getCity());
        address.setPincode(addressRequest.getPincode());
        Address updatedAddress = addressRepository.save(address);
        return AddressResponse.from(updatedAddress);
    }

    public void deleteAddress(Long userId, Long addressId) {
        getAddressById(userId, addressId);
        addressRepository.deleteById(addressId);
    }

    public Address getAddressById(Long userId, Long id) {
        Optional<Address> address = addressRepository.findByIdAndUserId(id, userId);
        if (address.isEmpty()) {
            throw new BaseException("Address with this Id Not Found for this user!!", HttpStatus.FORBIDDEN);
        }
        return address.get();
    }

    public static Address convertToAddress(AddressRequest addressRequest) {
        if (addressRequest == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setCountry(addressRequest.getCountry());
        address.setPincode(addressRequest.getPincode());
        return address;
    }
}