package com.shopping_app.shoppingApp.model.Address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;

    public static AddressResponse from(Address address) {
        if (address == null) {
            return null;
        }
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(address.getId());
        addressResponse.setStreet(address.getStreet());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setPincode(address.getPincode());
        addressResponse.setCountry(address.getCountry());
        return addressResponse;
    }
}