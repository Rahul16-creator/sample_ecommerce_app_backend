package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.model.Cart.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Cart.Response.CartResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import com.shopping_app.shoppingApp.repository.CartItemRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.shopping_app.shoppingApp.payload.MockPayload.getCartItemMockData;
import static com.shopping_app.shoppingApp.payload.MockPayload.getCartMockData;
import static com.shopping_app.shoppingApp.payload.MockPayload.getProductMockPayload;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserPrincipal applicationUser;

    @Mock
    private Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    private CartService cartService;

    @Test
    public void testAddItemsInCartSuccess() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(applicationUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(getCartMockData());
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProductMockPayload()));
        doReturn(getCartMockData()).when(cartRepository).findByUserId(anyLong());
        CartResponse cartResponse = cartService.addItemsInCart(MockPayload.getCartAddRequestPayload());
        assertNotNull(cartResponse);
        assertTrue(cartResponse.getCartItems().size() > 0);
    }

    @Test
    public void testAddItemsInCartFailure() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(applicationUser);
        doReturn(null).when(cartRepository).findByUserId(anyLong());
        assertThrows(NotFoundException.class, () -> cartService.addItemsInCart(MockPayload.getCartAddRequestPayload()));
    }

    @Test
    public void testGetCartSuccess() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(applicationUser);
        doReturn(getCartMockData()).when(cartRepository).findByUserId(anyLong());
        CartResponse cartResponse = cartService.getCart();
        assertNotNull(cartResponse);
    }

    @Test
    public void testGetCartFailure() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(applicationUser);
        doReturn(null).when(cartRepository).findByUserId(anyLong());
        assertThrows(NotFoundException.class, () -> cartService.getCart());
    }

    @Test
    public void testDeleteCartSuccess() {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCartItemMockData()));
        CartItemResponse response = cartService.deleteCartItem(1L);
        assertNotNull(response);
        assertEquals(getCartItemMockData().getProduct().getProductName(), response.getProductResponse().getProductName());
    }

    @Test
    public void testDeleteCartFailure() {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.deleteCartItem(1L));
    }

    @Test
    public void testUpdateCartItemsSuccess() {
        doReturn(Optional.ofNullable(getCartItemMockData())).when(cartItemRepository).findById(anyLong());
        CartItemResponse cartResponse = cartService.updateCartItem(MockPayload.getCartItemUpdateRequestPayload());
        assertNotNull(cartResponse);
        assertEquals(getCartItemMockData().getQuantity(), cartResponse.getQuantity());
    }

    @Test
    public void testUpdateCartItemsFailure() {
        doReturn(Optional.empty()).when(cartItemRepository).findById(anyLong());
        assertThrows(NotFoundException.class, () -> cartService.updateCartItem(MockPayload.getCartItemUpdateRequestPayload()));
    }
}