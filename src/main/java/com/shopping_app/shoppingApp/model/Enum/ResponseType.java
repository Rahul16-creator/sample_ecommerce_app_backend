package com.shopping_app.shoppingApp.model.Enum;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseType implements BaseEnum {
    SUCCESS("Success"),
    FAILURE("Failure");

    private String value;

    @Override
    public String getValue() {
        return this.value;
    }
}
