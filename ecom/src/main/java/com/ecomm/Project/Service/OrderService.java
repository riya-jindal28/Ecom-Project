package com.ecomm.Project.Service;

import com.ecomm.Project.Payload.OrderDTO;

import jakarta.transaction.Transactional;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressID, String paymentMethod, String pgName, String pgPaymentId,
            String pgStatus, String pgResponseMessage);
    
}
