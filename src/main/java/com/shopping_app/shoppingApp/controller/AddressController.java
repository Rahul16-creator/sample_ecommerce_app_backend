package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Address.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addAddress(@Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = addressService.addAddress(addressRequest);
        ApiResponse<AddressResponse> Response = new ApiResponse<AddressResponse>(HttpStatus.OK, "Address added Successfully", ResponseType.SUCCESS, addressResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(@Valid @RequestBody AddressRequest addressRequest, @PathVariable Long id) {
        AddressResponse addressResponse = addressService.updateAddress(addressRequest, id);
        ApiResponse<AddressResponse> Response = new ApiResponse<AddressResponse>(HttpStatus.OK, "Address updated Successfully", ResponseType.SUCCESS, addressResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id) {
        AddressResponse addressResponse = addressService.deleteAddress(id);
        ApiResponse<AddressResponse> Response = new ApiResponse<AddressResponse>(HttpStatus.OK, "Address deleted Successfully", ResponseType.SUCCESS, addressResponse);
        return new ResponseEntity<>(Response, Response.getCode());
    }
}