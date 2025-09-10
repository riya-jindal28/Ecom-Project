package com.ecomm.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.Project.Model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
