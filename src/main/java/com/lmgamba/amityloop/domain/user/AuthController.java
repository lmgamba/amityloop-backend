package com.lmgamba.amityloop.domain.user;

import com.lmgamba.amityloop.domain.user.dto.LoginRequest;
import com.lmgamba.amityloop.domain.user.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Marks this class as a REST controller — combines @Controller and @ResponseBody
// All methods return data directly (JSON), not view templates
@RestController
// Base path for all endpoints in this controller
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/auth/register
    // @RequestBody tells Spring to deserialize the JSON body into a RegisterRequest object
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String token = userService.register(request);
        // 200 OK with the JWT token in the response body
        return ResponseEntity.ok(token);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }
}