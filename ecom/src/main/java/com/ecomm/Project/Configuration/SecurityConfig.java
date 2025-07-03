package com.ecomm.Project.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for H2 console
            .headers(headers -> headers.frameOptions().disable()) // Allow H2 frames
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Allow public H2 access
                .anyRequest().authenticated() // All other endpoints need login
            )
            .formLogin(); // Enables Spring's default login page

        return http.build();
    }
}
