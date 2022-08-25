package com.shopping_app.shoppingApp.Exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends BaseException{

    public AuthException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public AuthException(String message) {
        super(message);
    }
}
