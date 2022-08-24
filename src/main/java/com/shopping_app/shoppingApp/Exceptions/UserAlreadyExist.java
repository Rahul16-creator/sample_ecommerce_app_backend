package com.shopping_app.shoppingApp.Exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExist extends BaseException {

    public UserAlreadyExist(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UserAlreadyExist(String message) {
        super(message);
    }
}
