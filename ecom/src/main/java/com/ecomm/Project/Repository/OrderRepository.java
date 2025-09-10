package com.ecomm.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomm.Project.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
}
