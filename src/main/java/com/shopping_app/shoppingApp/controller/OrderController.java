package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.ApiResponse;
import com.shopping_app.shoppingApp.model.Order.OrderAddRequest;
import com.shopping_app.shoppingApp.model.Order.OrderResponse;
import com.shopping_app.shoppingApp.model.Order.OrderUpdateRequest;
import com.shopping_app.shoppingApp.service.OrderService;
import com.shopping_app.shoppingApp.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/users/{userId}/orders")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> getAllUserOrders(@PathVariable Long userId) {
        List<OrderResponse> orderResponses = orderService.getUserOrders(userId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Orders fetched Successfully", orderResponses, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/orders/{orderId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> getUserOrderById(@PathVariable Long userId, @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getUserOrderById(userId, orderId);
        return new ResponseEntity<>(ResponseUtil.createResponse("Order fetched Successfully", orderResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/orders")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> addUserOrders(@PathVariable Long userId, @Valid @RequestBody OrderAddRequest orderAddRequest) {
        OrderResponse orderResponse = orderService.addOrders(userId, orderAddRequest);
        return new ResponseEntity<>(ResponseUtil.createResponse("Order placed Successfully", orderResponse, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    /**
     *  update order cancel status
     */
    @PutMapping("/users/{userId}/orders/{orderId}")
    @PreAuthorize("@accessControlService.isAuthenticated(#userId)")
    public ResponseEntity<ApiResponse> updateUserOrderStatus(@PathVariable Long userId, @PathVariable Long orderId, @Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse orderResponse = orderService.updateUserOrderStatus(userId, orderId, orderUpdateRequest);
        return new ResponseEntity<>(ResponseUtil.createResponse("Order Status Updated Successfully", orderResponse, HttpStatus.OK), HttpStatus.OK);
    }
}