package com.shopping_app.shoppingApp.repository;

import com.shopping_app.shoppingApp.domain.Address;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends AbstractRepository<Address> {

    List<Address> findByUserId(Long id);
}