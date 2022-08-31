package com.shopping_app.shoppingApp.model.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Address.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

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

    public static UserResponse from(User user) {
        if (user == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        if (user.getAddress() != null) {
            userResponse.setAddress(user.getAddress().stream().map(AddressResponse::from).collect(Collectors.toSet()));
        }

        return userResponse;
    }
}