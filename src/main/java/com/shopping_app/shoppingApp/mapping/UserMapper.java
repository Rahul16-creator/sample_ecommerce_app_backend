package com.shopping_app.shoppingApp.mapping;

import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Request.UserLoginRequest;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Response.UserLoginResponse;
import com.shopping_app.shoppingApp.model.Response.UserResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    @IterableMapping(qualifiedByName = "mapUserResponseData")
    List<UserResponse> convertToUserDomainList(List<User> users);

    User convertToUserDomain(UserRegisterRequest userRegisterRequest);

    @Named("mapUserResponseData")
    @IterableMapping(qualifiedByName = "mapAddressResponseData")
    UserResponse convertToUserResponse(User user);

    UserLoginResponse convertToUserLoginResponse(UserLoginRequest user, String token);
}