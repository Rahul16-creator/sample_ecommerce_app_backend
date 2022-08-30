package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.User;
import com.shopping_app.shoppingApp.repository.UserRepository;
import com.shopping_app.shoppingApp.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * Separated this change in separate file , reason not able to add it in userservie class it creating circular dependency issue
 * ex:
 *  JwtTokenProvider uses userservice
 *  userservice uses JwtTokenprovider
 * To avoid this , created this file.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserDetailServices implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("User With this email not exist", HttpStatus.NOT_FOUND);
        }
        return new UserPrincipal(user.get().getId(), user.get().getEmail(),user.get().getPassword());
    }
}
