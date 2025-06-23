package com.ecomm.Project.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ecomm.Project.Model.Product;
import com.ecomm.Project.Payload.ProductDTORequest;
import com.ecomm.Project.Payload.ProductDTOResponse;

public interface ProductService {

    ProductDTORequest addProduct(Product product, Long categoryID);
    
    ProductDTOResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTOResponse searchByCategory(Long categoryID);

    ProductDTOResponse searchProductByKeyword(String keyword);

    ProductDTORequest updateProduct(Long productID, Product product);

    ProductDTORequest deleteProduct(Long productID);

    ProductDTORequest updateProductImage(Long productID, MultipartFile image) throws IOException;
}
