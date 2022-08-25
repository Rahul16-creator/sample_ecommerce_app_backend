package com.shopping_app.shoppingApp.mapping;

import com.shopping_app.shoppingApp.domain.Cart;
import com.shopping_app.shoppingApp.domain.CartItems;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Response.CartItemResponse;
import com.shopping_app.shoppingApp.model.Response.CartResponse;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CartMapper extends ProductMapper {

    @IterableMapping(qualifiedByName = "mapCartItemResponseData")
    CartResponse convertToCartResposne(Cart cart);

    @Named("mapCartItemResponseData")
    @Mapping(source = "cartItems.product",target = "productResponse",qualifiedByName = "mapCartItemProductResponse" )
    CartItemResponse convertToCartItemResponse(CartItems cartItems);


    // TODO Check this bug
    @Named("mapCartItemProductResponse")
    default ProductResponse productResponse(Product product) {
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
