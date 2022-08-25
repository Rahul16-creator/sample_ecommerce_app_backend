package com.shopping_app.shoppingApp.model.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_app.shoppingApp.domain.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    private Set<AddressResponse> address;

    // TODO replace getter and  setter with lombok package

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AddressResponse> getAddress() {
        return address;
    }

    public void setAddress(Set<AddressResponse> address) {
        this.address = address;
    }
}
