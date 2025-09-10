package com.ecomm.Project.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemID;
    private ProductDTORequest product;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
