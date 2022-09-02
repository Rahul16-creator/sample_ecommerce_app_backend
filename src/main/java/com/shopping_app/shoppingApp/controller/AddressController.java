package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Address.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.service.AddressService;
import com.shopping_app.shoppingApp.utils.ResponseUtil;
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

    @PostMapping("/users/{userId}/addresses")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = addressService.addAddress(addressRequest, userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Address added Successfully", addressResponse, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/addresses/{addressId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequest addressRequest, @PathVariable Long addressId) {
        AddressResponse addressResponse = addressService.updateAddress(addressRequest, userId, addressId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Address updated Successfully", addressResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/addresses/{addressId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}