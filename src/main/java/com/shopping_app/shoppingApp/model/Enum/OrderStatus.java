package com.shopping_app.shoppingApp.model.Enum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus implements BaseEnum {

    BOOKED("booked"),
    CANCELLED("cancelled");

    private String value;

    @Override
    public String getValue() {
        return this.value;
    }
}
