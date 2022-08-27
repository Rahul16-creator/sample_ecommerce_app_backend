package com.shopping_app.shoppingApp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopping_app.shoppingApp.model.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Table(name="`Order`")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "order_status")
    private OrderStatus status;

    @Column(nullable = false, name = "tracking_number")
    private String trackingNumber;

    @Column(nullable = false, name = "delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
}
