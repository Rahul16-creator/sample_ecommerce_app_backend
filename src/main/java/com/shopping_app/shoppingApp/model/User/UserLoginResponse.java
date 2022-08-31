package com.shopping_app.shoppingApp.model.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse {

    private String email;

    private String token;

    public static UserLoginResponse from(UserLoginRequest user, String token) {
        if (user == null && token == null) {
            return null;
        }
        return UserLoginResponse.builder().email(user.getEmail()).token(token).build();
    }

}