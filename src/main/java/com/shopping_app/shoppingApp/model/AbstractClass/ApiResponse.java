package com.shopping_app.shoppingApp.model.AbstractClass;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ApiResponse<T> extends AbstractResponse {
    private T data;

    public ApiResponse(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public ApiResponse(HttpStatus httpStatus, String message, ResponseType type, T data) {
        super(type, message, httpStatus);
        this.data = data;
    }
    public ApiResponse(HttpStatus httpStatus, String message, ResponseType type) {
        super(type, message, httpStatus);
    }
}