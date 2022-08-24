package com.shopping_app.shoppingApp.model.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractResponse {
    private ResponseType status;
    private String message;
    private Integer count;
    private HttpStatus code;

    public AbstractResponse(HttpStatus httpStatusCode, String message) {
        this.status=ResponseType.FAILURE;
        this.message=message;
        this.count=0;
        this.code=httpStatusCode;
    }

}
