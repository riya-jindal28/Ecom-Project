package com.ecomm.Project.Payload;

public class CategoryDTORequest {
    private long categoryID;
    private String categoryName;

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

   

    public CategoryDTORequest(){

    }

    public CategoryDTORequest(long categoryID, String categoryName){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

     
}
