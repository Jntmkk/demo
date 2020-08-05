package xyz.example.demo.bean;

import lombok.Data;
import xyz.example.demo.models.Role;

import java.util.List;
import java.util.Set;

@Data
public class SignUpRequest {
    private String username;
//    private String email;
    private String password;
    private String address;
    private String privateKey;
    private Set<String> role;
}
