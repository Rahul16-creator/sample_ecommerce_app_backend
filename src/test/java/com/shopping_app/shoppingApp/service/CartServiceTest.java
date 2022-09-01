package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.CartItemResponse;
import com.shopping_app.shoppingApp.model.Cart.CartResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CartServiceTest extends AbstractServiceTest {

    @Autowired
    private CartService cartService;

    private Long cartId;
    private Long cartItemId;

    @Before
    public void setUp() {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId).get());
        cartRepository.save(cart);
        CartResponse cartResponse = cartService.addItemsToCart(MockPayload.getCartAddRequestPayload(), userId);
        cartId = cartResponse.getId();
        Set<CartItemResponse> cartItems = cartResponse.getCartItems();
        Optional<Long> id = cartItems.stream().map(e -> e.getId()).findFirst();
        cartItemId = id.get();
    }

    @Test
    public void testAddItemToCartSuccess() {
        CartAddRequest cartAddRequestPayload = MockPayload.getCartAddRequestPayload();
        cartAddRequestPayload.setProduct_id(2L);
        CartResponse cartResponse = cartService.addItemsToCart(cartAddRequestPayload, userId);
        assertNotNull(cartResponse);
        assertTrue(cartResponse.getCartItems().size() > 0);
    }

    @Test
    public void testAddItemToCartFailure_AlreadyExist() {
        try {
            cartService.addItemsToCart(MockPayload.getCartAddRequestPayload(), userId);
        } catch (BaseException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        }
    }

    @Test
    public void testAddItemToCartFailure() {
        try {
            cartService.addItemsToCart(MockPayload.getCartAddRequestPayload(), -1L);
        } catch (NotFoundException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
            assertEquals("Cart not found!!", ex.getMessage());
        }
    }

    @Test
    public void testGetCartSuccess() {
        CartResponse cartResponse = cartService.getCart(userId);
        assertNotNull(cartResponse);
        assertTrue(cartResponse.getCartItems().size() > 0);
    }

    @Test
    public void testGetCartFailure() {
        try {
            cartService.getCart(-1L);
        } catch (NotFoundException ex) {
            assertEquals("Cart not found!!", ex.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        }
    }

    @Test
    public void testUpdateCartItemSuccess() {
        CartItemResponse cartItemResponse = cartService.updateCartItem(MockPayload.getCartItemUpdateRequestPayload(), cartId, cartItemId, userId);
        assertNotNull(cartItemResponse);
        int requestQuantity = MockPayload.getCartItemUpdateRequestPayload().getQuantity();
        assertEquals(requestQuantity, cartItemResponse.getQuantity());
    }

    @Test
    public void testUpdateCartItemFailure_cartNotFound() {
        try {
            cartService.updateCartItem(MockPayload.getCartItemUpdateRequestPayload(), cartId, cartItemId, -1L);
        } catch (NotFoundException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
            assertEquals("Cart not found!!", ex.getMessage());
        }
    }

    @Test
    public void testUpdateCartItemFailure_accessDenied() {
        try {
            cartService.updateCartItem(MockPayload.getCartItemUpdateRequestPayload(), -1L, cartItemId, userId);
        } catch (BaseException ex) {
            assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
        }
    }

    @Test
    public void testDeleteCartItemSuccess() {
        cartService.deleteCartItem(cartId, cartItemId, userId);
    }

    @Test
    public void testDeleteCartItemFailure_cartNotFound() {
        try {
            cartService.deleteCartItem(cartId, cartItemId, -1L);
        } catch (NotFoundException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
            assertEquals("Cart not found!!", ex.getMessage());
        }
    }

    @Test
    public void testDeleteCartItemFailure_accessDenied() {
        try {
            cartService.deleteCartItem(-1L, cartItemId, userId);
        } catch (BaseException ex) {
            assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
        }
    }

}