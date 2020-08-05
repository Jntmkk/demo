package xyz.example.demo.bean;

import lombok.Data;
import sun.security.util.Password;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
