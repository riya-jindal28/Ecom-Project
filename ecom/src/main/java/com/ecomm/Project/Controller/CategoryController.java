package com.ecomm.Project.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.Project.Configuration.AppConstants;
import com.ecomm.Project.Payload.CategoryDTORequest;
import com.ecomm.Project.Payload.CategoryDTOResponse;
import com.ecomm.Project.Service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;   
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(){
        return "hello admin";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String user(){
        return "hello user";
    }

    @GetMapping("/api/public/categories")
    public CategoryDTOResponse getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                @RequestParam(name = "sortBy", required = true) String sortBy,
                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder){
        CategoryDTOResponse categoryDTOResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return categoryDTOResponse;
    }

    @PostMapping("/api/public/category")
    public ResponseEntity<CategoryDTORequest> createCategory(@Valid @RequestBody CategoryDTORequest category){
        CategoryDTORequest categoryDTORequest = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDTORequest, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/category/delete/{categoryID}")
    public ResponseEntity<CategoryDTORequest> deleteCategory(@PathVariable Long categoryID){
        
            CategoryDTORequest delCategoryDTORequest = categoryService.deleteCategory(categoryID);
            return new ResponseEntity<>(delCategoryDTORequest, HttpStatus.OK);
       
    }

    @PutMapping("/api/public/category/update/{categoryID}")
    public ResponseEntity<CategoryDTORequest> updateCategory(@Valid @RequestBody CategoryDTORequest categoryDTO, 
                                                @PathVariable Long categoryID){
       
            CategoryDTORequest savedCategory = categoryService.updateCategory(categoryDTO, categoryID);
            return new ResponseEntity<>(savedCategory, HttpStatus.OK);
        
    }
}
