package com.shopping_app.shoppingApp.Exceptions;

import com.shopping_app.shoppingApp.model.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleApplicationException(BaseException baseException) {
        ApiResponse apiResponse = new ApiResponse(baseException.getHttpStatus(), baseException.getMessage());
        return new ResponseEntity<>(apiResponse, baseException.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConStraintViolationException(ConstraintViolationException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    // todo check for better way to handle global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalApplicationException(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("Internal Server Error");
        sb.append("/ " + ex.getMessage());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, sb.toString());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
