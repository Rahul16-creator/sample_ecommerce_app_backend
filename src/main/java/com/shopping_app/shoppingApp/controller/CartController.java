package com.shopping_app.shoppingApp.controller;


import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Response.AddressResponse;
import com.shopping_app.shoppingApp.model.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Response.CartResponse;
import com.shopping_app.shoppingApp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartController {

    private final CartService cartService;


    @GetMapping("/")
    public ResponseEntity<ApiResponse> getCart() {
        CartResponse cartResponse = cartService.getCart();
        return new ResponseEntity<>(getResponse("Item added to Your Cart Successfully", 1, cartResponse), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addItemInCart(@RequestBody CartAddRequest cartAddRequest) {
        CartResponse cartResponse = cartService.addItemsInCart(cartAddRequest);
        return new ResponseEntity<>(getResponse("Item added to Your Cart Successfully", 1, cartResponse), HttpStatus.OK);
    }

    public ApiResponse getResponse(String message, Integer count, Object data) {
        return ApiResponse.builder()
                .data(data)
                .code(HttpStatus.OK)
                .message(message)
                .count(count)
                .status(ResponseType.SUCCESS)
                .build();
    }

}
