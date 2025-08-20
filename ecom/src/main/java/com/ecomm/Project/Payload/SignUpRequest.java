package com.ecomm.Project.Payload;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 30)
    private String Username;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
