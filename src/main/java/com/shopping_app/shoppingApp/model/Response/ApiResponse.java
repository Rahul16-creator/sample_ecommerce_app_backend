package com.shopping_app.shoppingApp.model.Response;

import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> extends AbstractResponse {
    private T data;

    public ApiResponse(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public ApiResponse(HttpStatus httpStatus, String message, Integer count, ResponseType type, T data) {
        super(type, message, count, httpStatus);
        this.data = data;
    }
}
