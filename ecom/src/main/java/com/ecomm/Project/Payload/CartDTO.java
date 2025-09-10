package com.ecomm.Project.Payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<ProductDTORequest> products = new ArrayList<>();
}
