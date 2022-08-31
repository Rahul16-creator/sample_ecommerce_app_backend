package com.shopping_app.shoppingApp.model.AbstractClass;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder(builderMethodName = "baseBuilder")
public class AbstractResponse {
    private ResponseType status;
    private String message;
    private HttpStatus code;

    public AbstractResponse(HttpStatus httpStatusCode, String message) {
        this.status = ResponseType.FAILURE;
        this.message = message;
        this.code = httpStatusCode;
    }
}