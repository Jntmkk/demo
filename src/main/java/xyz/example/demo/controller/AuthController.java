package xyz.example.demo.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.example.demo.bean.JwtResponse;
import xyz.example.demo.bean.LoginRequest;
import xyz.example.demo.bean.SignUpRequest;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.exception.UserAlreadyExistsException;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.ERole;
import xyz.example.demo.models.Role;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.RoleRepository;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.impl.UserDetailsImpl;
import xyz.example.demo.utils.JwtUtils;
import xyz.example.demo.web3j.EthCallUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(tags = "处理用户管理")
public class AuthController {
    @Autowired
    UserContract userContract;
    @Autowired
    TaskContract taskContract;
    @Autowired
    DeviceContract deviceContract;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EthCallUtil ethCallUtil;
    @Autowired
    JwtUtils jwtUtils;

    @ApiOperation(value = "登录")
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {

        List<Object> taskInformation = ethCallUtil.getValue("getTaskInformation", "0x61C048AaC3Cf99FE97217A8b28F6ce32EB8B8ADE",new Long(0));
        log.info(JSON.toJSONString(taskInformation));
//        CrowdBCTask object = ethCallUtil.getObject(taskInformation, CrowdBCTask.class);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
//                userDetails.getEmail(),
                roles);
//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
////                userDetails.getEmail(),
//                roles));
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public String logout() {
        return "success";
    }

    @ApiOperation(value = "注册")
    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException();
//            return "Error: Username is already taken!";
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
        }

//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setAddress(signUpRequest.getAddress());
        user.setRoles(roles);
        user.setPrivateKey(signUpRequest.getPrivateKey());
        userRepository.save(user);
//        register.register(user.getAddress(), user.getUsername(), user.getPassword(), "").send();
        userContract.register(signUpRequest.getAddress(),signUpRequest.getUsername(),signUpRequest.getPassword(),"").send();
        return "User registered successfully!";
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
