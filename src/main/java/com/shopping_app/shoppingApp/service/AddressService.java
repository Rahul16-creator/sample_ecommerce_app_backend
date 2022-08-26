package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.model.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Response.AddressResponse;

public interface AddressService {

    AddressResponse addAddress(AddressRequest addressRequest);

    AddressResponse updateAddress(AddressRequest addressRequest, Long id);

    AddressResponse deleteAddress(Long id);
}