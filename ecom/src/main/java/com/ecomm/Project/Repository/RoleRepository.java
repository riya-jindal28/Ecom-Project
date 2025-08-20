package com.ecomm.Project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.Project.Model.AppRole;
import com.ecomm.Project.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByRoleName(AppRole appRole);

    
}
