package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/user/{userId}/address")
    @PreAuthorize("@authenticationService.isAuthenticate(#userId)")
    public ResponseEntity<ApiResponse> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = addressService.addAddress(addressRequest, userId);
        ApiResponse<AddressResponse> Response = new ApiResponse<AddressResponse>(HttpStatus.CREATED, "Address added Successfully", ResponseType.SUCCESS, addressResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @PutMapping("/user/{userId}/address/{addressId}")
    @PreAuthorize("@authenticationService.isAuthenticate(#userId)")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequest addressRequest, @PathVariable Long addressId) {
        AddressResponse addressResponse = addressService.updateAddress(addressRequest, userId, addressId);
        ApiResponse<AddressResponse> Response = new ApiResponse<AddressResponse>(HttpStatus.OK, "Address updated Successfully", ResponseType.SUCCESS, addressResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @DeleteMapping("/user/{userId}/address/{addressId}")
    @PreAuthorize("@authenticationService.isAuthenticate(#userId)")
    public ResponseEntity deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}