package com.shopping_app.shoppingApp.service.impl;

import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Request.CartAddRequest;
import com.shopping_app.shoppingApp.model.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Response.CartResponse;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import com.shopping_app.shoppingApp.repository.CartRepository;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import com.shopping_app.shoppingApp.service.CartService;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse getCart() {
        Cart userCart = cartRepository.findByUserId(getUserId());
        System.out.println(userCart.getCartItems().size());
        Set<CartItemResponse> cartItemResponses = userCart.getCartItems().stream().map(this::convertToCartItemResponse).collect(Collectors.toSet());
        return CartResponse.builder().cartItems(cartItemResponses).build();
    }

    @Override
    public CartResponse addItemsInCart(CartAddRequest cartRequest) {
        Cart userCart = cartRepository.findByUserId(getUserId());
        Optional<Product> product = productRepository.findById(cartRequest.getCartItemRequest().getProduct_id());
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product.get());
        cartItem.setQuantity(cartRequest.getCartItemRequest().getQuantity());
        cartItem.setCart(userCart);
        userCart.getCartItems().add(cartItem);
        Cart cart = cartRepository.save(userCart);
        Set<CartItemResponse> cartItemResponses = cart.getCartItems().stream().map(this::convertToCartItemResponse).collect(Collectors.toSet());
        return CartResponse.builder().cartItems(cartItemResponses).build();
    }

    public long getUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    public CartItemResponse convertToCartItemResponse(CartItems cartItems) {
        if ( cartItems == null ) {
            return null;
        }
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setProductResponse( productResponse( cartItems.getProduct() ) );
        if ( cartItems.getId() != null ) {
            cartItemResponse.setId( cartItems.getId() );
        }
        cartItemResponse.setQuantity( cartItems.getQuantity() );
        return cartItemResponse;
    }

    public ProductResponse productResponse(Product product) {
        if ( product == null ) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductPrice( product.getPrice() );
        productResponse.setProductDescription( product.getDescription() );
        productResponse.setId( product.getId() );
        productResponse.setProductName( product.getProductName() );
        productResponse.setAvailableQuantity( product.getAvailableQuantity() );
        return productResponse;
    }
}
