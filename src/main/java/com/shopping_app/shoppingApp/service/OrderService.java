package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Order;
import com.shopping_app.shoppingApp.domain.OrderItems;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.mapping.AddressMapper;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.model.Cart.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import com.shopping_app.shoppingApp.model.Order.Request.OrderAddRequest;
import com.shopping_app.shoppingApp.model.Order.Request.OrderUpdateRequest;
import com.shopping_app.shoppingApp.model.Order.Response.OrderItemResponse;
import com.shopping_app.shoppingApp.model.Order.Response.OrderResponse;
import com.shopping_app.shoppingApp.repository.CartItemRepository;
import com.shopping_app.shoppingApp.repository.OrderRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final ProductMapper productMapper;

    private final CartItemRepository cartItemRepository;

    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToOrderResponse).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        return convertToOrderResponse(fetchOrderById(id));
    }

    public OrderResponse addOrder(OrderAddRequest orderAddRequest) {
        Cart cart = cartService.getCartById(orderAddRequest.getCartId());
        Address address = addressService.fetchAddressById(orderAddRequest.getShippingAddressId());
        User user = userService.fetchUserById(getUserId());
        Order order = new Order();
        Set<OrderItems> orderItems = new HashSet<>();
        Set<CartItems> cartItems = cart.getCartItems();
        for (CartItems e : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setQuantity(e.getQuantity());
            orderItem.setProduct(e.getProduct());
            orderItem.setTotalPrice((e.getProduct().getPrice() * e.getQuantity()));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.BOOKED);
        order.setShippingAddress(address);
        order.setDeliveryDate(LocalDate.now().plusDays(3));
        order.setUser(user);
        order.setTrackingNumber(UUID.randomUUID().toString());
        Order savedOrder = orderRepository.save(order);
        if (null != savedOrder.getId()) {
            removeItemsFromCart(cartItems);
        }
        return convertToOrderResponse(savedOrder);
    }

    public OrderResponse updateOrder(OrderUpdateRequest orderUpdateRequest) {
        Order order = fetchOrderById(orderUpdateRequest.getId());
        order.setStatus(orderUpdateRequest.getOrderStatus());
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponse(updatedOrder);
    }

    public Order fetchOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundException("Order with this Id is Not Found", HttpStatus.NOT_FOUND);
        }
        return order.get();
    }

    public OrderResponse convertToOrderResponse(Order order) {

        return OrderResponse.builder()
                .orderStatus(order.getStatus())
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .shippingAddress(addressMapper.convertToAddressResponse(order.getShippingAddress()))
                .trackingNumber(order.getTrackingNumber())
                .orderItems(getOrderItemResponse(order.getOrderItems())).build();
    }

    public Set<OrderItemResponse> getOrderItemResponse(Set<OrderItems> orderItems) {
        Set<OrderItemResponse> orderItemResponses = new HashSet<>();
        for (OrderItems o : orderItems) {
            orderItemResponses.add(OrderItemResponse.builder().quantity(o.getQuantity()).totalPrice(o.getTotalPrice()).product(productMapper.convertToProductResponse(o.getProduct())).build());
        }
        return orderItemResponses;
    }

    public void removeItemsFromCart(Set<CartItems> cartItems) {
        for (CartItems e : cartItems) {
            cartItemRepository.deleteCartItems(e.getId());
        }
    }

    public long getUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
}
