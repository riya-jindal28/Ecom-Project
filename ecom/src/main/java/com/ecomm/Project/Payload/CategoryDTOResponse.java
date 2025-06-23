package com.ecomm.Project.Payload;

import java.util.List;

public class CategoryDTOResponse {

    private List<CategoryDTORequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
    private String sortBy;
    private String sortOrder;
    
    public CategoryDTOResponse() {
    }

    public CategoryDTOResponse(List<CategoryDTORequest> content, Integer pageNumber, Integer pageSize,
            Long totalElements, Integer totalPages, Boolean lastPage, String sortBy, String sortOrder) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.lastPage = lastPage;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public List<CategoryDTORequest> getContent() {
        return content;
    }

    public void setContent(List<CategoryDTORequest> content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    
}
