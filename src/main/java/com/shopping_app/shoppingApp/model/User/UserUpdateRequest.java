package com.shopping_app.shoppingApp.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UserUpdateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;
}