package xyz.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import xyz.example.demo.bean.User;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/add")
    public boolean addUser(@RequestBody User user) {
        return false;
    }
    @GetMapping("/find/{id}")
    public User findById(@PathVariable("id") int id) {
        return new User();
    }
    @PutMapping("/update")
    public boolean update(@RequestBody User user) {
        return true;
    }
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return true;
    }
}
