package com.ecomm.Project.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecomm.Project.Exception.ResourceNotFoundException;
import com.ecomm.Project.Model.Category;
import com.ecomm.Project.Model.Product;
import com.ecomm.Project.Payload.ProductDTORequest;
import com.ecomm.Project.Payload.ProductDTOResponse;
import com.ecomm.Project.Repository.CategoryRepository;
import com.ecomm.Project.Repository.ProductRepository;

@Service
public class ProductserviceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTORequest addProduct(Product product, Long categoryID) {
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTORequest.class);
    }

    @Override
    public ProductDTOResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                            ? Sort.by(sortBy).ascending()
                            : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTORequest> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class))
                .toList();

        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setContent(productDTOs);
        productDTOResponse.setPageNumber(pageProducts.getNumber());
        productDTOResponse.setPageSize(pageProducts.getSize());
        productDTOResponse.setTotalElements(pageProducts.getTotalElements());
        productDTOResponse.setTotalPages(pageProducts.getTotalPages());
        productDTOResponse.setLastPage(pageProducts.isLast());

        
        return productDTOResponse;
    }

    @Override
    public ProductDTOResponse searchByCategory(Long categoryID) {
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() ->
                new ResourceNotFoundException("Category", "categoryID", categoryID));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTORequest> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class))
                .toList();

        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setContent(productDTOs);
        return productDTOResponse;
    }

    @Override
    public ProductDTOResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTORequest> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class))
                .toList();

        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setContent(productDTOs);
        return productDTOResponse;
    }

    @Override
    public ProductDTORequest updateProduct(Long productID, Product product) {
        Product productFormDB = productRepository.findById(productID)
                          .orElseThrow(() -> new ResourceNotFoundException("Product", "productID", productID));

        productFormDB.setProductName(product.getProductName());
        productFormDB.setDescription(product.getDescription());
        productFormDB.setQuantity(product.getQuantity());
        productFormDB.setDiscount(product.getDiscount());
        productFormDB.setPrice(product.getPrice());
        productFormDB.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(productFormDB);
        return modelMapper.map(savedProduct, ProductDTORequest.class);
    }

    @Override
    public ProductDTORequest deleteProduct(Long productID) {
        Product product = productRepository.findById(productID)
                          .orElseThrow(() -> new ResourceNotFoundException("Product", "productID", productID));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTORequest.class);
    }

    @Override
    public ProductDTORequest updateProductImage(Long productID, MultipartFile image) throws IOException {
        //Get the product from DB
        Product productDB = productRepository.findById(productID)
                          .orElseThrow(() -> new ResourceNotFoundException("Product", "productID", productID));

        //Upload Image to the Server
        //Get the file name of the uploaded image

        String path = "images/";
        String fileName = uploadImage(path, image);

        //Updating the new file name to the product
        productDB.setImage(fileName);
 
        //Save Updated Product
        Product updatedProduct = productRepository.save(productDB);

        //Return DTO after mapping product to DTO

        return modelMapper.map(updatedProduct, ProductDTORequest.class);             
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //File name of the current file
        String originalFileName = file.getOriginalFilename();

        //Generate a unique file name 
        String randomID = UUID.randomUUID().toString();
        String fileName = randomID.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.pathSeparator + fileName;

        //Check If path exist
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        //Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //Returning File Name 
        return fileName;
    }
}
