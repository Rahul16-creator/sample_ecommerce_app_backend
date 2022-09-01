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
    private final CartItemRepository cartItemRepository;

    private final ProductService productService;

    public CartResponse getUserCart(Long userId) {
        Cart userCart = getCartByUserId(userId);
        Set<CartItemResponse> cartItemResponses = userCart.getCartItems().stream().map(CartItemResponse::from).collect(Collectors.toSet());
        return CartResponse.from(userCart, cartItemResponses);
    }

    public CartResponse addItemsToCart(CartAddRequest cartRequest, Long userId) {
        Cart userCart = getCartByUserId(userId);
        Product product = productService.findProductById(cartRequest.getProduct_id());
        Optional<CartItems> cartItemProduct = cartItemRepository.findByCartIdAndProductId(userCart.getId(), cartRequest.getProduct_id());
        checkAvailableQuantityOfProduct(product, cartRequest.getQuantity());
        CartItems cartItem = new CartItems();
        if (cartItemProduct.isEmpty()) {
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            cartItem.setCart(userCart);
            userCart.getCartItems().add(cartItem);
        } else {
            increaseQuantityInExistingItem(userCart, cartRequest.getProduct_id(), cartRequest.getQuantity());
        }
        Cart cart = cartRepository.save(userCart);
        Set<CartItemResponse> cartItemResponses = cart.getCartItems().stream().map(CartItemResponse::from).collect(Collectors.toSet());
        return CartResponse.from(cart, cartItemResponses);
    }

    public void checkAvailableQuantityOfProduct(Product product, int quantity) {
        if (product.getAvailableQuantity() < quantity) {
            throw new BaseException("Stock unavailable!!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteCartItem(Long cartId, Long cartItemId, Long userId) {
        validateCart(userId, cartId);
        getCartItemByCartId(cartItemId, cartId);
        cartItemRepository.deleteById(cartItemId);
    }

    public void validateCart(Long userId, Long cartId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getId() != cartId) {
            throw new BaseException("Invalid cart id!!", HttpStatus.FORBIDDEN);
        }
    }

    public CartItemResponse updateCartItem(CartItemUpdateRequest cartItemUpdateRequest, Long cartId, Long cartItemId, Long userId) {
        validateCart(userId, cartId);
        CartItems cartItem = getCartItemByCartId(cartItemId, cartId);
        Product product = productService.findProductById(cartItem.getProduct().getId());
        checkAvailableQuantityOfProduct(product, cartItemUpdateRequest.getQuantity());
        cartItem.setQuantity(cartItemUpdateRequest.getQuantity());
        cartItemRepository.save(cartItem);
        return CartItemResponse.from(cartItem);
    }

    public Cart getCartByUserId(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            log.error("Cart was not found this issue might be happen when registration happen");
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

    public void increaseQuantityInExistingItem(Cart userCart, Long productId, int quantity) {
        for (CartItems item : userCart.getCartItems()) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(item.getQuantity() + quantity);
            }
        }
    }

}