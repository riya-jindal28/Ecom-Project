package com.ecomm.Project.Service;
import com.ecomm.Project.Payload.CategoryDTORequest;
import com.ecomm.Project.Payload.CategoryDTOResponse;

public interface CategoryService {

    // List<Category> getAllCategories();
    CategoryDTOResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    // void createCategory(Category category);
    CategoryDTORequest createCategory(CategoryDTORequest category);

    // String deleteCategory(Long categoryID);

    CategoryDTORequest deleteCategory(Long categoryID);
    // Category updateCategory(Category category, Long categoryID);

    CategoryDTORequest updateCategory(CategoryDTORequest category, Long categoryID);

    
}