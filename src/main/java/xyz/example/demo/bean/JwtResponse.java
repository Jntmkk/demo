package xyz.example.demo.bean;

import lombok.Data;
import xyz.example.demo.models.Role;

import java.util.List;


@Data
public class JwtResponse {
    private String token, type;
    private Long id;
    private String username;
    private List<Role> roles;

    public JwtResponse() {
    }

    public JwtResponse(String token, Long id, String username, List roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
