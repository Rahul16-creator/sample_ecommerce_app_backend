package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItem;
import com.shopping_app.shoppingApp.domain.Order;
import com.shopping_app.shoppingApp.domain.OrderItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import com.shopping_app.shoppingApp.model.Order.CreateOrderRequest;
import com.shopping_app.shoppingApp.model.Order.OrderResponse;
import com.shopping_app.shoppingApp.repository.CartItemRepository;
import com.shopping_app.shoppingApp.repository.OrderRepository;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final AddressService addressService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    public OrderResponse getUserOrderById(Long userId, Long orderId) {
        return OrderResponse.from(getOrderById(userId, orderId));
    }

    public OrderResponse createOrder(Long userId, CreateOrderRequest orderAddRequest) {
        Cart cart = cartService.getCartByUserId(userId);
        cartService.validateCart(userId, orderAddRequest.getCartId());

        Address address = addressService.getAddressById(userId, orderAddRequest.getShippingAddressId());
        User user = userService.getUserById(userId);

        Order order = new Order();
        Set<OrderItems> orderItems = addOrderItems(cart.getCartItems(), order);

        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.BOOKED); // Default
        order.setShippingAddress(address);
        order.setDeliveryDate(LocalDate.now().plusDays(3)); // adding three days by default
        order.setUser(user);
        order.setTrackingNumber(UUID.randomUUID().toString());

        Order savedOrder = orderRepository.save(order);

        // once order placed , do-- cart empty and update inventory count of product
        updateProductAvailability(savedOrder);
        removeItemsFromCart(cart.getCartItems());

        return OrderResponse.from(savedOrder);
    }

    public void updateProductAvailability(Order order) {
        for (OrderItems orderItems : order.getOrderItems()) {
            Product product = orderItems.getProduct();
            product.setAvailableQuantity(product.getAvailableQuantity() - orderItems.getQuantity());
            productRepository.save(product);
        }
    }

    public Set<OrderItems> addOrderItems(Set<CartItem> cartItems, Order order) {
        Set<OrderItems> orderItems = new HashSet<>();

        for (CartItem e : cartItems) {
            // Check product availability
            cartService.checkProductAvailability(e.getProduct(), e.getQuantity());

            OrderItems orderItem = new OrderItems();
            orderItem.setQuantity(e.getQuantity());
            orderItem.setProduct(e.getProduct());
            orderItem.setTotalPrice((e.getProduct().getPrice() * e.getQuantity()));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    public OrderResponse cancelUserOrder(Long userId, Long orderId) {
        Order order = getOrderById(userId, orderId);
        // check Order status validity
        checkOrderValidity(order);

        // update order cancel status
        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);

        // when order is cancelled , update the product availability count
        addProductAvailability(updatedOrder);
        return OrderResponse.from(updatedOrder);
    }

    public void addProductAvailability(Order order) {
        for (OrderItems orderItems : order.getOrderItems()) {
            Product product = orderItems.getProduct();
            product.setAvailableQuantity(product.getAvailableQuantity() + orderItems.getQuantity());
            productRepository.save(product);
        }
    }

    public void checkOrderValidity(Order order) {
        /**
         *  if the order is already cancelled , then exception will be throw here
         */
        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new BaseException("This order is already cancelled", HttpStatus.BAD_REQUEST);
        }
    }

    public Order getOrderById(Long userId, Long orderId) {
        Optional<Order> order = orderRepository.findByIdAndUserId(orderId, userId);
        if (!order.isPresent()) {
            throw new BaseException("Order not found", HttpStatus.FORBIDDEN);
        }
        return order.get();
    }

    public void removeItemsFromCart(Set<CartItem> cartItems) {
        for (CartItem e : cartItems) {
            cartItemRepository.deleteCartItems(e.getId());
        }
    }
}