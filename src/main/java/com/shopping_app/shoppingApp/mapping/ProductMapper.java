package com.shopping_app.shoppingApp.mapping;

import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.model.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    Product convertToProductDomain(ProductRequest productRequest);

    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "description", target = "productDescription")
    ProductResponse convertToProductResponse(Product product);
}