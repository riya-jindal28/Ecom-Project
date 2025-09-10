package com.ecomm.Project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table (name = "CartItems")
@AllArgsConstructor
@NoArgsConstructor
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CartItemsID;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double discount;

    private Double productprice;


}
