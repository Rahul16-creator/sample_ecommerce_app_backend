package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.model.Cart.CartAddResponse;
import com.shopping_app.shoppingApp.model.Order.CreateOrderRequest;
import com.shopping_app.shoppingApp.model.Order.OrderResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderServiceTest extends AbstractServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    private Long orderId;
    private Long addressId;
    private Long cartId;

    @Before
    public void setUp() {
        Address address = MockPayload.getAddressMockData();
        address.setUser(userRepository.findById(userId).get());
        Address saveAddress = addressRepository.save(address);
        addressId = saveAddress.getId();

        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId).get());
        cartRepository.save(cart);
        Optional<Cart> userCart = cartRepository.findByUserId(userId);
        CartAddResponse cartResponse = cartService.addItemsToCart(MockPayload.getCartAddRequestPayload(), userId, userCart.get().getId());
        cartId = cartResponse.getId();

        CreateOrderRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(cartId);
        orderAddMockerRequest.setShippingAddressId(addressId);
        OrderResponse orderResponse = orderService.createOrder(userId, orderAddMockerRequest);
        orderId = orderResponse.getId();
    }

    @Test
    public void testAddOrderSuccess() {
        CreateOrderRequest orderAddMockerRequest = MockPayload.getOrderAddMockerRequest();
        orderAddMockerRequest.setCartId(cartId);
        orderAddMockerRequest.setShippingAddressId(addressId);
        OrderResponse orderResponse = orderService.createOrder(userId, orderAddMockerRequest);
        assertNotNull(orderResponse);
    }

    @Test
    public void testAddOrderFailure_AddressNotFound() {
        try {
            CreateOrderRequest orderAddRequest = MockPayload.getOrderAddMockerRequest();
            orderAddRequest.setCartId(cartId);
            orderAddRequest.setShippingAddressId(-1L);
            orderService.createOrder(userId, orderAddRequest);
        } catch (BaseException ex) {
            assertEquals("Address with this Id Not Found for this user!!", ex.getMessage());
        }
    }

    @Test
    public void testGetAllOrderSuccess() {
        List<OrderResponse> userOrders = orderService.getUserOrders(userId);
        assertTrue(userOrders.size() > 0);
    }

    @Test
    public void testGetOrderByIdSuccess() {
        OrderResponse orderResponse = orderService.getUserOrderById(userId, orderId);
        assertNotNull(orderResponse);
    }

    @Test
    public void testGetOrderByIdFailure() {
        try {
            orderService.getUserOrderById(userId, -1L);
        } catch (BaseException ex) {
            assertEquals("Order not found", ex.getMessage());
        }
    }

    @Test
    public void testUpdateOrderSuccess() {
        OrderResponse orderResponse = orderService.updateUserOrderStatus(userId, orderId, MockPayload.getOrderUpdateMockerRequest());
        assertNotNull(orderResponse);
    }

    @Test
    public void testUpdateOrderFailure_OrderNotFound() {
        try {
            orderService.updateUserOrderStatus(userId, -1L, MockPayload.getOrderUpdateMockerRequest());
        } catch (BaseException ex) {
            assertEquals("Order not found", ex.getMessage());
        }
    }

    @Test
    public void testUpdateOrderFailure_OrderAlreadyCancelled() {
        try {
            orderService.updateUserOrderStatus(userId, orderId, MockPayload.getOrderUpdateMockerRequest());
            orderService.updateUserOrderStatus(userId, orderId, MockPayload.getOrderUpdateMockerRequest());
        } catch (BaseException ex) {
            assertEquals("This order is already cancelled", ex.getMessage());
        }
    }
}