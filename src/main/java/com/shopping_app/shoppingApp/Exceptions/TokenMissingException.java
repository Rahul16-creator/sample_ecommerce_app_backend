package com.shopping_app.shoppingApp.Exceptions;

import org.springframework.http.HttpStatus;

public class TokenMissingException extends BaseException {

    public TokenMissingException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public TokenMissingException(String message) {
        super(message);
    }
}