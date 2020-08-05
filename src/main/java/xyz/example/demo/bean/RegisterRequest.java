package xyz.example.demo.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String address;
}
