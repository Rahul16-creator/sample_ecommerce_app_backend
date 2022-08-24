package com.shopping_app.shoppingApp.model.Enum;

import com.shopping_app.shoppingApp.model.Enum.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole implements BaseEnum {
    CUSTOMER("customer"),
    SELLER("seller"),
    ADMIN("admin");
    private String value;

    @Override
    public String getValue() {
        return this.value;
    }
}
