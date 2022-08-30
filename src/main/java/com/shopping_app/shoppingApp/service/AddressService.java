package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.repository.AddressRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    public AddressResponse addAddress(AddressRequest addressRequest) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = convertToAddress(addressRequest);
        User user = userService.fetchUserById(principal.getId());
        address.setUser(user);
        Address saveAddress = addressRepository.save(address);
        return convertToAddressResponse(saveAddress);
    }

    public AddressResponse updateAddress(AddressRequest addressRequest, Long id) {
        Address address = fetchAddressById(id);
        address.setStreet(addressRequest.getStreet());
        address.setState(addressRequest.getState());
        address.setCountry(addressRequest.getCountry());
        address.setCity(addressRequest.getCity());
        address.setPincode(addressRequest.getPincode());
        Address updatedAddress = addressRepository.save(address);
        return convertToAddressResponse(updatedAddress);
    }

    public AddressResponse deleteAddress(Long id) {
        Address address = fetchAddressById(id);
        addressRepository.deleteById(id);
        return convertToAddressResponse(address);
    }

    public Address fetchAddressById(Long id) {
        Optional<Address> isAddressExist = addressRepository.findById(id);
        if (isAddressExist.isEmpty()) {
            throw new NotFoundException("Address Id Not Found!!", HttpStatus.NOT_FOUND);
        }
        return isAddressExist.get();
    }

    public Address convertToAddress(AddressRequest addressRequest) {
        if ( addressRequest == null ) {
            return null;
        }

        Address address = new Address();

        address.setStreet( addressRequest.getStreet() );
        address.setCity( addressRequest.getCity() );
        address.setState( addressRequest.getState() );
        address.setCountry( addressRequest.getCountry() );
        address.setPincode( addressRequest.getPincode() );

        return address;
    }

    public AddressResponse convertToAddressResponse(Address address) {
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

}