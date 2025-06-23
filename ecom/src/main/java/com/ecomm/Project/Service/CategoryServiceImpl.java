package com.ecomm.Project.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.ecomm.Project.Exception.APIException;
import com.ecomm.Project.Exception.ResourceNotFoundException;
import com.ecomm.Project.Model.Category;
import com.ecomm.Project.Payload.CategoryDTORequest;
import com.ecomm.Project.Payload.CategoryDTOResponse;
import com.ecomm.Project.Repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // public List<Category> getAllCategories(){
    //     return categoryRepository.findAll();
    // }

    public CategoryDTOResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        // List<Category> categories = categoryRepository.findAll();
        List<CategoryDTORequest> categoryDTOS = categories.stream()
        .map(category -> modelMapper.map(category, CategoryDTORequest.class))
        .toList();

        CategoryDTOResponse categoryDTOResponse = new CategoryDTOResponse();
        categoryDTOResponse.setContent(categoryDTOS);
        categoryDTOResponse.setPageNumber(categoryPage.getNumber());
        categoryDTOResponse.setPageSize(categoryPage.getSize());
        categoryDTOResponse.setTotalElements(categoryPage.getTotalElements());
        categoryDTOResponse.setTotalPages(categoryPage.getTotalPages());
        categoryDTOResponse.setLastPage(categoryPage.isLast());
        categoryDTOResponse.setSortBy(sortBy);
        categoryDTOResponse.setSortOrder(sortOrder);
        
        return categoryDTOResponse;
    }

    // public void createCategory(Category category){
    //     Category savedCategory = categoryRepository.findBycategoryName(category.getCategoryName());
    //     if (savedCategory!=null) {   
    //         throw new APIException("Category with the name " + category.getCategoryName() + " already exists");
            
    //     }
    //     categoryRepository.save(category);
    // }


    public CategoryDTORequest createCategory(CategoryDTORequest categoryDTO){
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findBycategoryName(category.getCategoryName());
        if (savedCategory!=null) {   
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists");
            
        }
        Category savedCategoryD = categoryRepository.save(category);
        CategoryDTORequest savedCategoryDTORequest = modelMapper.map(savedCategoryD, CategoryDTORequest.class);
        return savedCategoryDTORequest;
    }


    public CategoryDTORequest deleteCategory(Long categoryID){
        Category delCategory = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        categoryRepository.delete(delCategory);
        CategoryDTORequest delCategoryDTORequest = modelMapper.map(delCategory, CategoryDTORequest.class);
        return delCategoryDTORequest;
        // return "Category with Category ID : " + categoryID + " deleted Successfully"; 
        // // List<Category> categories = categoryRepository.findAll();
        // Category category = categories.stream()
        //                     .filter(c -> categoryID.equals(c.getCategoryID()))
        //                     .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));         
    }

    public CategoryDTORequest updateCategory(CategoryDTORequest categoryDTORequest, Long categoryID){
        Category category = modelMapper.map(categoryDTORequest, Category.class);
        Category savedCategory = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));
        category.setCategoryID(categoryID);

        savedCategory = categoryRepository.save(category);
        CategoryDTORequest savedcategoryDTORequest = modelMapper.map(savedCategory, CategoryDTORequest.class);
        return savedcategoryDTORequest;
        

        // Optional<Category> optionalcategory = categories.stream()
        //                     .filter(c -> categoryID.equals(categoryID))
        //                     .findFirst();                  
        // if(optionalcategory.isPresent()){
        //     Category existingCategory = optionalcategory.get();
        //     existingCategory.setCategoryName(category.getCategoryName());
        //     Category savedCategory = categoryRepository.save(existingCategory);
        //     return savedCategory;
        // }                   
        // else{
        //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
        // }
    }
}
