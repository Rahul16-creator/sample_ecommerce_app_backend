package com.shopping_app.shoppingApp.Exceptions;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleApplicationException(BaseException baseException) {
        ApiResponse apiResponse = new ApiResponse(baseException.getHttpStatus(), baseException.getMessage());
        return new ResponseEntity<>(apiResponse, baseException.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        Optional<String> errors = fieldErrors.stream().map(fieldError -> "'" + fieldError.getField() + "'" + " parameter " + fieldError.getDefaultMessage()).findFirst();
        String error = "Please make a valid request !";
        if (errors.isPresent()) error = errors.get();
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, error);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConStraintViolationException(ConstraintViolationException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthException(AuthenticationException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalApplicationException(Exception ex) {
        StringBuilder sb = new StringBuilder();
        if (ex.getMessage() == null) {
            sb.append("Internal Server Error");
        } else {
            sb.append(ex.getMessage());
        }
        ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, sb.toString());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}