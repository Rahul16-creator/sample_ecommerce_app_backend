package com.shopping_app.shoppingApp.mapping;

import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.model.Request.UserRegisterRequest;
import com.shopping_app.shoppingApp.model.Response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    User convertToUserDomain(UserRegisterRequest userRegisterRequest);

    UserResponse convertToUserResponse(User user);

}
