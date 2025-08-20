package com.ecomm.Project.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.Project.Model.AppRole;
import com.ecomm.Project.Model.Role;
import com.ecomm.Project.Model.User;
import com.ecomm.Project.Payload.LoginRequest;
import com.ecomm.Project.Payload.LoginResponse;
import com.ecomm.Project.Payload.MessageResponse;
import com.ecomm.Project.Payload.SignUpRequest;
import com.ecomm.Project.Repository.RoleRepository;
import com.ecomm.Project.Repository.UserRepository;
import com.ecomm.Project.Security.JWT.JwtUtils;
import com.ecomm.Project.Security.Services.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch(AuthenticationException exception){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("Status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), jwtToken, roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return ResponseEntity
                   .badRequest().body(new MessageResponse("Error : Username is already taken!"));
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity
                   .badRequest()
                   .body(new MessageResponse("Error : Email already taken!"));
        }

        User user = new User(signUpRequest.getUsername(), 
        signUpRequest.getEmail(),
        passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();  
        Set<Role> roles = new HashSet<>();

        if(strRoles==null){
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
            .orElseThrow(
                () -> new RuntimeException("Error: Role is not found")
            );

            roles.add(userRole);
        }
        else{
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not Found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not Found"));
                        roles.add(sellerRole);
                        break;

                    default :
                         Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not Found"));
                        roles.add(userRole);
                        break;
                
                    
                        
                }
            }

            );
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
    }

}
