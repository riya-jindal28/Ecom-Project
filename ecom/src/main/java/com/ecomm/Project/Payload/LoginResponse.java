package com.ecomm.Project.Payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    public LoginResponse(long id, String username, List<String> roles) {
    this.id = id;
    this.username = username;
    this.roles = roles;
    }
    private Long id;
    private String username;
    private String jwtToken;
    private List<String> roles;
}
