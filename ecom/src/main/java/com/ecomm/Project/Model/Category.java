package com.ecomm.Project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryID;

    
    @NotBlank(message = "Category Name is required")
    private String categoryName;


    public Category(){

    }
    
    public Category(long categoryID, String categoryName){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public long getCategoryID(){
        return categoryID;
    }

    public void setCategoryID(long categoryID){
        this.categoryID = categoryID;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
