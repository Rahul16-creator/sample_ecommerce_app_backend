package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}