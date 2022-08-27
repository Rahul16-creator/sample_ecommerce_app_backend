package com.shopping_app.shoppingApp.model.User.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private Set<AddressResponse> address;
}