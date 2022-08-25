package com.shopping_app.shoppingApp.mapping;

import com.shopping_app.shoppingApp.domain.Address;
import com.shopping_app.shoppingApp.model.Request.AddressRequest;
import com.shopping_app.shoppingApp.model.Response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AddressMapper {

    @Mapping(target = "user",ignore = true)
    Address convertToAddress(AddressRequest addressRequest);

    @Named("mapAddressResponseData")
    AddressResponse convertToAddressResponse(Address address);

    AddressResponse convertToAddressResponse(AddressRequest addressRequest);
}
