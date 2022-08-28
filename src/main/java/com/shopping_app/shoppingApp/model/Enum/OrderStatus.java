package com.shopping_app.shoppingApp.model.Enum;

import com.shopping_app.shoppingApp.model.Enum.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus implements BaseEnum {

    BOOKED("booked"),
    OFD("out_for_delivery"),
    SHIPPED("shipped"),
    FAILED("failed"),
    DELIVERED("delivered");

    private String value;

    @Override
    public String getValue() {
        return this.value;
    }
}
