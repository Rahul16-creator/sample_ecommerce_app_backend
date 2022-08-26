package com.shopping_app.shoppingApp.model.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
}