package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.configs.JwtTokenConfig;
import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.helpers.AuthRequest;
import jp.co.axa.apidemo.helpers.AuthResponse;
import jp.co.axa.apidemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/auth")
public class UserController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenConfig jwtTokenConfig;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenConfig.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
            return ResponseEntity.ok().body(response);
        }catch (BadCredentialsException ex) {
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "error");
            map.put("error", ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

    @PostMapping("/register")
    public  ResponseEntity<?> register(@RequestBody @Valid AuthRequest request) {
        HashMap<String, String> map = new HashMap<>();
        if(userService.findUserByEmail(request.getEmail())) {
            map.put("message", "User Already Exists");
            map.put("email", request.getEmail());
            map.put("status", "error");
            return ResponseEntity.ok().body(map);
        } else {
            try {
                User saveUserInfo = new User();
                saveUserInfo.setEmail(request.getEmail());
                saveUserInfo.setPassword(request.getPassword());
                boolean saveUser = userService.saveUser(saveUserInfo);
                if (saveUser) {
                    map.put("message", "User Saved Successfully");
                    map.put("status", "success");
                } else {
                    map.put("message", "Something went wrong during saved user");
                    map.put("status", "error");
                }
                map.put("email", saveUserInfo.getEmail());
                return ResponseEntity.ok().body(map);
            } catch (Exception ex) {
                map.put("message", "Something went wrong during saved user");
                map.put("email", "");
                map.put("ex", ex.getLocalizedMessage());
                map.put("status", "error");
                return ResponseEntity.ok().body(map);
            }
        }

    }

}
