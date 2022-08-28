package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.mapping.AddressMapper;
import com.shopping_app.shoppingApp.mapping.AddressMapperImpl;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.model.Address.Response.AddressResponse;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import com.shopping_app.shoppingApp.model.Order.Response.OrderResponse;
import com.shopping_app.shoppingApp.repository.OrderRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.shopping_app.shoppingApp.payload.MockPayload.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private AddressService addressService;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private OrderService orderService;

    private AddressMapper addressMapperImpl;


    @Before
    public void setUp() {
        addressMapperImpl = new AddressMapperImpl();
    }

    @Test
    public void testGetAllOrder() {
        when(orderRepository.findAll()).thenReturn(List.of(getOrderMockPayload()));
        List<OrderResponse> order = orderService.getAllOrder();
        assertNotNull(order);
        assertEquals(1, order.size());
    }

    @Test
    public void testGetAllOrderEmpty() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());
        List<OrderResponse> order = orderService.getAllOrder();
        assertEquals(0, order.size());
    }

    @Test
    public void testGetOrderById() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(getOrderMockPayload()));
        OrderResponse order = orderService.getOrderById(1L);
        assertNotNull(order);
        assertEquals(getOrderMockPayload().getStatus(), order.getOrderStatus());
    }

    @Test
    public void testGetOrderByIdException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    public void testAddOrderSuccess() {
        AddressResponse addressResponse = addressMapperImpl.convertToAddressResponse(getAddressMockData());
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(addressMapper.convertToAddressResponse(any(Address.class))).thenReturn(addressResponse);
        when(productMapper.convertToProductResponse(any())).thenReturn(getProductResponseMockPayload());
        when(cartService.getCartById(anyLong())).thenReturn(getCartMockData());
        when(addressService.fetchAddressById(anyLong())).thenReturn(getAddressMockData());
        when(userService.fetchUserById(anyLong())).thenReturn(getUserMockdata());
        when(orderRepository.save(any())).thenReturn(getOrderMockPayload());
        OrderResponse orderResponse = orderService.addOrder(getOrderAddMockerRequest());
        assertNotNull(orderResponse);
        assertEquals(OrderStatus.BOOKED, orderResponse.getOrderStatus());
    }

    @Test
    public void testAddOrderFailure() {
        when(cartService.getCartById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> orderService.addOrder(getOrderAddMockerRequest()));
    }

    @Test
    public void testOrderUpdateSuccess() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(getOrderMockPayload()));
        when(orderRepository.save(any())).thenReturn(getOrderMockPayload());
        OrderResponse orderResponse = orderService.updateOrder(getOrderUpdateRequestMockPayload());
        assertNotNull(orderResponse);
        assertEquals(getOrderMockPayload().getStatus(), orderResponse.getOrderStatus());
    }

    @Test
    public void testOrderUpdateException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.updateOrder(getOrderUpdateRequestMockPayload()));
    }
}
