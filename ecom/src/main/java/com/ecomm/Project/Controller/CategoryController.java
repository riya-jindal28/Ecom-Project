package com.ecomm.Project.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.Project.Configuration.AppConstants;
// import com.ecomm.Project.JWT.JwtUtils;
// import com.ecomm.Project.JWT.LoginRequest;
// import com.ecomm.Project.JWT.LoginResponse;
import com.ecomm.Project.Payload.CategoryDTORequest;
import com.ecomm.Project.Payload.CategoryDTOResponse;
import com.ecomm.Project.Service.CategoryService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;   
    }

    // @Autowired
    // private AuthenticationManager authenticationManager;

    // @Autowired 
    // private JwtUtils jwtUtils;


    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping("/admin")
    // public String admin(){
    //     return "hello admin";
    // }

    // @PreAuthorize("hasRole('USER')")
    // @GetMapping("/user")
    // public String user(){
    //     return "hello user";
    // }

    // @PostMapping("/signin")
    // public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    //     Authentication authentication;
    //     try{
    //         authentication = authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(
    //             loginRequest.getUsername(),
    //             loginRequest.getPassword()
    //         ));
    //     }catch(AuthenticationException exception){
    //         Map<String, Object> map = new HashMap<>();
    //         map.put("message", "Bad Credentials");
    //         map.put("status", false);

    //         return new ResponseEntity<Object >(map, HttpStatus.NOT_FOUND);
    //     }
    //     SecurityContextHolder.getContext().setAuthentication(authentication);
    //     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //     String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
    //     List<String> roles = userDetails.getAuthorities().stream()
    //     .map(item->item.getAuthority())
    //     .collect(Collectors.toList());

    //     LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken,roles);

    //     return ResponseEntity.ok(response);
    // }
    

    @GetMapping("/public/categories")
    public CategoryDTOResponse getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                @RequestParam(name = "sortBy", required = true) String sortBy,
                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder){
        CategoryDTOResponse categoryDTOResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return categoryDTOResponse;
    }

    @PostMapping("/public/category")
    public ResponseEntity<CategoryDTORequest> createCategory(@Valid @RequestBody CategoryDTORequest category){
        CategoryDTORequest categoryDTORequest = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDTORequest, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/category/delete/{categoryID}")
    public ResponseEntity<CategoryDTORequest> deleteCategory(@PathVariable Long categoryID){
        
            CategoryDTORequest delCategoryDTORequest = categoryService.deleteCategory(categoryID);
            return new ResponseEntity<>(delCategoryDTORequest, HttpStatus.OK);
       
    }

    @PutMapping("/public/category/update/{categoryID}")
    public ResponseEntity<CategoryDTORequest> updateCategory(@Valid @RequestBody CategoryDTORequest categoryDTO, 
                                                @PathVariable Long categoryID){
       
            CategoryDTORequest savedCategory = categoryService.updateCategory(categoryDTO, categoryID);
            return new ResponseEntity<>(savedCategory, HttpStatus.OK);
        
    }
}
