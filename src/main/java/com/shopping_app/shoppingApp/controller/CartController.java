package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Cart.Request.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Cart.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.Response.CartResponse;
import com.shopping_app.shoppingApp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartController {

    private final CartService cartService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getCart() {
        CartResponse cartResponse = cartService.getCart();
        return new ResponseEntity<>(getResponse("Cart Items fetched Successfully", cartResponse), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addItemInCart(@Valid @RequestBody CartAddRequest cartAddRequest) {
        CartResponse cartResponse = cartService.addItemsInCart(cartAddRequest);
        return new ResponseEntity<>(getResponse("Item added to Your Cart Successfully", cartResponse), HttpStatus.OK);
    }

    @PutMapping("/cartItem")
    public ResponseEntity<ApiResponse> updateCartItem(@Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest) {
        CartItemResponse response = cartService.updateCartItem(cartItemUpdateRequest);
        return new ResponseEntity<>(getResponse("Cart Item updated Successfully", response), HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{id}")
    public ResponseEntity<ApiResponse> deleteICartItem(@PathVariable Long id) {
        CartItemResponse response = cartService.deleteCartItem(id);
        return new ResponseEntity<>(getResponse("Cart Item deleted Successfully", response), HttpStatus.OK);
    }

    public ApiResponse getResponse(String message, Object data) {
        return ApiResponse.builder()
                .data(data)
                .code(HttpStatus.OK)
                .message(message)
                .status(ResponseType.SUCCESS)
                .build();
    }
}
