package com.ecomm.Project.Service;

import java.util.List;

import com.ecomm.Project.Payload.CartDTO;

import jakarta.transaction.Transactional;

public interface CartService {

     CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
