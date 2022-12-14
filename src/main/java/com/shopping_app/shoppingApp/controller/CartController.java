package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.CartAddResponse;
import com.shopping_app.shoppingApp.model.Cart.CartItemResponse;
import com.shopping_app.shoppingApp.model.Cart.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Cart.CartResponse;
import com.shopping_app.shoppingApp.service.CartService;
import com.shopping_app.shoppingApp.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartController {

    private final CartService cartService;

    @GetMapping("/users/{userId}/cart")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId) {
        CartResponse cartResponse = cartService.getUserCart(userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Cart fetched Successfully", cartResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/cart/{cartId}/cartItems")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> addItemToCart(@PathVariable Long cartId, @PathVariable Long userId, @Valid @RequestBody CartAddRequest cartAddRequest) {
        CartAddResponse cartResponse = cartService.addItemsToCart(cartAddRequest, userId, cartId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Item added to your Cart Successfully", cartResponse, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/cart/{cartId}/cartItems/{cartItemId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long userId, @PathVariable Long cartId, @PathVariable Long cartItemId, @Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest) {
        CartItemResponse response = cartService.updateCartItem(cartItemUpdateRequest, cartId, cartItemId, userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Cart Item updated Successfully", response, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/cart/{cartId}/cartItems/{cartItemId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartId, cartItemId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
