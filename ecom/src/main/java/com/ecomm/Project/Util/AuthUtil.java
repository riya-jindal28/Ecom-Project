package com.ecomm.Project.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ecomm.Project.Model.User;
import com.ecomm.Project.Repository.UserRepository;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));

        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));

        return user.getUserID();
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
        return user;

    }


}

