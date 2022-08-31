package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.utils.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    public boolean isAuthenticate(String userId) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userId.equals(String.valueOf(principal.getId()));
    }
}