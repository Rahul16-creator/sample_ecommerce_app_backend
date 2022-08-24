package com.shopping_app.shoppingApp.Exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

   public NotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public  NotFoundException(String message) {
        super(message);
    }
}
