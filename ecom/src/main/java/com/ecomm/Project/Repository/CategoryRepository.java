package com.ecomm.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecomm.Project.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    Category findBycategoryName(String categoryName);

} 