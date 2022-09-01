package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.BaseException;
import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Cart.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Cart.CartItemResponse;
import com.shopping_app.shoppingApp.model.Cart.CartResponse;
import com.shopping_app.shoppingApp.repository.CartItemRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartResponse getCart(Long userId) {
        Cart userCart = getCartByUserId(userId);
        Set<CartItemResponse> cartItemResponses = userCart.getCartItems().stream().map(CartItemResponse::from).collect(Collectors.toSet());
        return CartResponse.from(userCart, cartItemResponses);
    }

    public CartResponse addItemsToCart(CartAddRequest cartRequest, Long userId) {
        Cart userCart = getCartByUserId(userId);
        Optional<CartItems> cartItemProduct = cartItemRepository.findByproductId(cartRequest.getProduct_id());
        if (cartItemProduct.isPresent()) {
            throw new BaseException("Product Already added in cart", HttpStatus.BAD_REQUEST);
        }
        Optional<Product> product = productRepository.findById(cartRequest.getProduct_id());
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product.get());
        cartItem.setQuantity(cartRequest.getQuantity());
        cartItem.setCart(userCart);
        userCart.getCartItems().add(cartItem);
        Cart cart = cartRepository.save(userCart);
        Set<CartItemResponse> cartItemResponses = cart.getCartItems().stream().map(CartItemResponse::from).collect(Collectors.toSet());
        return CartResponse.from(cart, cartItemResponses);
    }

    public void deleteCartItem(Long cartId, Long cartItemId, Long userId) {
        validateCart(userId, cartId);
        getCartItemByCartId(cartItemId, cartId);
        cartItemRepository.deleteById(cartItemId);
    }

    public void validateCart(Long userId, Long cartId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getId() != cartId) {
            throw new BaseException("Cart with this id not found for this user!!", HttpStatus.FORBIDDEN);
        }
    }

    public CartItemResponse updateCartItem(CartItemUpdateRequest cartItemUpdateRequest, Long cartId, Long cartItemId, Long userId) {
        validateCart(userId, cartId);
        CartItems cartItem = getCartItemByCartId(cartItemId, cartId);
        cartItem.setQuantity(cartItemUpdateRequest.getQuantity());
        cartItemRepository.save(cartItem);
        return CartItemResponse.from(cartItem);
    }

    public Cart getCartByUserId(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            log.error("Cart was not found this issue might be happen registration happen");
            throw new NotFoundException("Cart not found!!", HttpStatus.NOT_FOUND);
        }
        return cart.get();
    }

    public CartItems getCartItemByCartId(Long cartItemId, Long cartId) {
        Optional<CartItems> cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cartId);
        if (cartItem.isEmpty()) {
            throw new BaseException("CartItem not found", HttpStatus.FORBIDDEN);
        }
        return cartItem.get();
    }

}