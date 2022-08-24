package com.shopping_app.shoppingApp.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private HttpStatus httpStatus;
    BaseException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }

    BaseException(String message) {
        super(message);
    }
}
