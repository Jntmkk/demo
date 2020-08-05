package xyz.example.demo.controller;

import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.example.demo.models.User;


import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user/")
@Validated
@Api(tags = "user information")
public class UserController {
//    @Autowired
//    private Register register;
    @Data
    public static class UserInfo {
        Set<String> roles = new HashSet<>();
        String introduction = "I am a super administrator";
        String avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
        String name = "Super Admin";
        int reputation = 6;
    }

    @GetMapping("info")
    public User info() throws Exception {

        return null;
    }

}
