package com.ecomm.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomm.Project.Model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
