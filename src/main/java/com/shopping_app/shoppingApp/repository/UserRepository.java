package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AbstractRepository<User> {

    Optional<User> findByEmail(String email);
}
