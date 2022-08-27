package com.shopping_app.shoppingApp.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@EqualsAndHashCode(callSuper=false)
public class Product extends BaseEntity {

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Float price;

    private String description;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;
}