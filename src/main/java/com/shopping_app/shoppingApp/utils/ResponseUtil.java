package com.shopping_app.shoppingApp.utils;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import org.springframework.http.HttpStatus;

public class ResponseUtil {
    public static ApiResponse createResponse(String message, Object data, HttpStatus httpStatus) {
        return ApiResponse.builder()
                .data(data)
                .code(httpStatus)
                .message(message)
                .status(ResponseType.SUCCESS)
                .build();
    }
}
