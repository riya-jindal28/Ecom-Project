package com.ecomm.Project.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecomm.Project.Configuration.AppConstants;
import com.ecomm.Project.Model.Product;
import com.ecomm.Project.Payload.ProductDTORequest;
import com.ecomm.Project.Payload.ProductDTOResponse;
import com.ecomm.Project.Service.ProductService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("api/admin/categories/{categoryID}/product")
    public ResponseEntity<ProductDTORequest> addProduct(@RequestBody Product product, @PathVariable Long categoryID){
       ProductDTORequest productDTO = productService.addProduct(product, categoryID);
       return new ResponseEntity<>(productDTO,HttpStatus.CREATED);
    }

    @GetMapping("api/public/products")
    public ResponseEntity<ProductDTOResponse> getAllProducts(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
        )
    {
        ProductDTOResponse productDTOResponse = productService.getAllProducts(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productDTOResponse, HttpStatus.OK);
    }

    @GetMapping("api/public/categories/{categoryID}/products")
    public ResponseEntity<ProductDTOResponse> getProductsByCategory(@PathVariable Long categoryID){
        ProductDTOResponse productDTOResponse = productService.searchByCategory(categoryID);
        return new ResponseEntity<>(productDTOResponse, HttpStatus.OK);
    }

    @GetMapping("api/public/products/keyword/{keyword}")
    public ResponseEntity<ProductDTOResponse> getProductsByKeyword(@PathVariable String keyword){
        ProductDTOResponse productDTOResponse = productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productDTOResponse, HttpStatus.FOUND);
    }

    @PutMapping("api/admin/products/{productID}")
    public ResponseEntity<ProductDTORequest> updateProduct(@RequestBody Product product,
                                                        @PathVariable Long productID){

        ProductDTORequest updateproductDTO = productService.updateProduct(productID, product);
        return new ResponseEntity<>(updateproductDTO, HttpStatus.OK);
    }

    @DeleteMapping("api/admin/products/{productID}")
    public ResponseEntity<ProductDTORequest> deleteProduct(@PathVariable Long productID){
        ProductDTORequest deleteProductDTO = productService.deleteProduct(productID);
        return new ResponseEntity<>(deleteProductDTO, HttpStatus.OK);
    }

    @PutMapping("api/admin/products/{productID}/image")
    public ResponseEntity<ProductDTORequest> updateProductImage(@PathVariable Long productID, @RequestParam("image")MultipartFile image) throws IOException {
       
        ProductDTORequest updateImageDTO = productService.updateProductImage(productID, image);
        return new ResponseEntity<>(updateImageDTO,HttpStatus.OK);
    }
}
