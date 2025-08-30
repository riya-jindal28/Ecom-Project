package com.ecomm.Project.Controller;

import java.net.ResponseCache;
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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("Status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error : Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email already taken!"));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(
                            () -> new RuntimeException("Error: Role is not found"));

            roles.add(userRole);
        } else {
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

                    default:
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
 

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        }
        else{
            return "";
        }
            
    }

    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                cookie.toString())
                .body(new MessageResponse("You've been signed out!!"));
    }
}
