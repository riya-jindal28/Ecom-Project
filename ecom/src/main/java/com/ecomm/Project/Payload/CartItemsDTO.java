package com.ecomm.Project.Payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDTO {

    private Long CartItemsId;
    private CartDTO cartDTO;
    private ProductDTORequest productDTO;
    private Integer quantity;
    private double productPrice = 0.0;
    private double discount;
    
}
