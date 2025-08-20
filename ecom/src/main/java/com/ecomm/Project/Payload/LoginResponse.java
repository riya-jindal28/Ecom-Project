package com.ecomm.Project.Payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    private Long id;
    private String username;
    private String jwtToken;
    private List<String> roles;
}
