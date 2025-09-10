package com.ecomm.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.Project.Model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}