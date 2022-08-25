package com.shopping_app.shoppingApp.service.impl;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.AddressMapper;
import com.shopping_app.shoppingApp.model.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Response.AddressResponse;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.service.AddressService;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserServiceImpl userService;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse addAddress(AddressRequest addressRequest) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressMapper.convertToAddress(addressRequest);
        User user = userService.fetchUserById(principal.getId());
        address.setUser(user);
        Address saveAddress = addressRepository.save(address);
        return addressMapper.convertToAddressResponse(saveAddress);
    }

    @Override
    public AddressResponse updateAddress(AddressRequest addressRequest, Long id) {
        Address address = fetchAddressById(id);
        if (StringUtils.isNotBlank(addressRequest.getStreet())) {
            address.setStreet(addressRequest.getStreet());
        }
        if (StringUtils.isNotBlank(addressRequest.getState())) {
            address.setState(addressRequest.getState());
        }
        if (StringUtils.isNotBlank(addressRequest.getCountry())) {
            address.setCountry(addressRequest.getCountry());
        }
        if (StringUtils.isNotBlank(addressRequest.getCity())) {
            address.setCity(addressRequest.getCity());
        }
        if (StringUtils.isNotBlank(addressRequest.getPincode())) {
            address.setPincode(addressRequest.getPincode());
        }
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.convertToAddressResponse(updatedAddress);
    }

    @Override
    public AddressResponse deleteAddress(Long id) {
        Address address = fetchAddressById(id);
        addressRepository.deleteById(id);
        return addressMapper.convertToAddressResponse(address);
    }

    public Address fetchAddressById(Long id) {
        Optional<Address> isAddressExist = addressRepository.findById(id);
        if (isAddressExist.isEmpty()) {
            throw new NotFoundException("Address Id Not Found!!", HttpStatus.NOT_FOUND);
        }
        return isAddressExist.get();
    }
}
