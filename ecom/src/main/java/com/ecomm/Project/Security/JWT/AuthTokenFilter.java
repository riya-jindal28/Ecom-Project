package com.ecomm.Project.Security.JWT;

import java.io.IOException;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecomm.Project.Security.Services.UserDetailsServiceImpl;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse resposnse, FilterChain filterChain) throws ServletException, IOException{
        logger.debug("AuthTokenFilter called for URI : {}", request.getRequestURI());
        try{
            String jwt = parseJwt(request);
            if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUserNameFromJWTToken(jwt);
                UserDetails  userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Roles from JWT : {}", userDetails.getAuthorities());

           }
        }catch(Exception e){
            logger.error("Cannot set user Authentication", e);
        }
        filterChain.doFilter(request, resposnse);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJWTFronHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}
