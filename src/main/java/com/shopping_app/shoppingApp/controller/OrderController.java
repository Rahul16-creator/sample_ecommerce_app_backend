package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Enum.ResponseType;
import com.shopping_app.shoppingApp.model.Order.Request.OrderAddRequest;
import com.shopping_app.shoppingApp.model.Order.Request.OrderUpdateRequest;
import com.shopping_app.shoppingApp.model.Order.Response.OrderResponse;
import com.shopping_app.shoppingApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllOrder() {
        List<OrderResponse> orderResponses = orderService.getAllOrder();
        return new ResponseEntity<>(getResponse("Orders fetched Successfully", orderResponses), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return new ResponseEntity<>(getResponse("Order fetched Successfully", orderResponse), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addItemInCart(@Valid @RequestBody OrderAddRequest orderAddRequest) {
        OrderResponse orderResponse = orderService.addOrder(orderAddRequest);
        return new ResponseEntity<>(getResponse("Order created Successfully", orderResponse), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> updateOrder(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse orderResponse = orderService.updateOrder(orderUpdateRequest);
        return new ResponseEntity<>(getResponse("Order Updated Successfully", orderResponse), HttpStatus.OK);
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