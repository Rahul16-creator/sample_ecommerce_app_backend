package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Cart.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Cart.Request.CartItemUpdateRequest;
import com.shopping_app.shoppingApp.model.Cart.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Cart.Response.CartResponse;
import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import com.shopping_app.shoppingApp.repository.CartItemRepository;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    public CartResponse getCart() {
        Cart userCart = getCartById(getUserId());
        Set<CartItemResponse> cartItemResponses = userCart.getCartItems().stream().map(this::convertToCartItemResponse).collect(Collectors.toSet());
        return CartResponse.builder().id(userCart.getId()).cartItems(cartItemResponses).build();
    }

    public CartResponse addItemsInCart(CartAddRequest cartRequest) {
        Cart userCart = getCartById(getUserId());
        Optional<Product> product = productRepository.findById(cartRequest.getCartItemRequest().getProduct_id());
        boolean isNewItem = checkProductExistInCart(userCart, product.get());
        CartItems cartItem = new CartItems();
        if (!isNewItem) {
            cartItem.setProduct(product.get());
            cartItem.setQuantity(cartRequest.getCartItemRequest().getQuantity());
            cartItem.setCart(userCart);
            userCart.getCartItems().add(cartItem);
        } else {
            increaseQuantityInExistintItem(userCart, product.get(), cartRequest.getCartItemRequest().getQuantity());
        }
        Cart cart = cartRepository.save(userCart);
        Set<CartItemResponse> cartItemResponses = cart.getCartItems().stream().map(this::convertToCartItemResponse).collect(Collectors.toSet());
        return CartResponse.builder().id(cart.getId()).cartItems(cartItemResponses).build();
    }

    public CartItemResponse deleteCartItem(Long id) {
        CartItems cartItem = getCartItemById(id);
        cartItemRepository.deleteById(id);
        return convertToCartItemResponse(cartItem);
    }

    public CartItemResponse updateCartItem(CartItemUpdateRequest cartItemUpdateRequest) {
        CartItems cartItem = getCartItemById(cartItemUpdateRequest.getId());
        cartItem.setQuantity(cartItemUpdateRequest.getQuantity());
        cartItemRepository.save(cartItem);
        return convertToCartItemResponse(cartItem);
    }

    public Cart getCartById(Long id) {
        Cart cartItem = cartRepository.findByUserId(id);
        if (cartItem == null) {
            throw new NotFoundException("Cart id is wrong", HttpStatus.NOT_FOUND);
        }
        return cartItem;
    }

    public CartItems getCartItemById(Long id) {
        Optional<CartItems> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            throw new NotFoundException("CartItem id is wrong", HttpStatus.NOT_FOUND);
        }
        return cartItem.get();
    }

    public void increaseQuantityInExistintItem(Cart userCart, Product product, int quantity) {
        for (CartItems item : userCart.getCartItems()) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
            }
        }
    }

    public long getUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    public boolean checkProductExistInCart(Cart cart, Product product) {
        Set<CartItems> cartItems = cart.getCartItems();
        for (CartItems e : cartItems) {
            if (null != e.getProduct() && e.getProduct().getId() == product.getId()) {
                return true;
            }
        }
        return false;
    }

    public CartItemResponse convertToCartItemResponse(CartItems cartItems) {
        if (cartItems == null) {
            return null;
        }
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setProductResponse(productResponse(cartItems.getProduct()));
        if (cartItems.getId() != null) {
            cartItemResponse.setId(cartItems.getId());
        }
        cartItemResponse.setQuantity(cartItems.getQuantity());
        return cartItemResponse;
    }

    public ProductResponse productResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductPrice(product.getPrice());
        productResponse.setProductDescription(product.getDescription());
        productResponse.setId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setAvailableQuantity(product.getAvailableQuantity());
        return productResponse;
    }

}
