package xyz.example.demo.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.UserRepository;
@Component
public class UserTokenUtil {
    public UserTokenUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserRepository userRepository;

    public String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public User getUser() {
        return userRepository.findByUsername(getUserName()).get();
    }
}
