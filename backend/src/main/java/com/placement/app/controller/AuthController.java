package com.placement.app.controller;

import com.placement.app.config.JwtUtils;
import com.placement.app.dto.AuthDTOs.*;
import com.placement.app.entity.User;
import com.placement.app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        String role = (req.getRole() == null || req.getRole().isEmpty()) ? "STUDENT" : req.getRole().toUpperCase();
        User user = new User(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()), role, req.getFullName(), req.getCompanyName(), req.getDepartment(), req.getGpa());
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole(), user.getId());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole(), user.getFullName(), user.getId(), user.getCompanyName(), user.getDepartment(), user.getGpa()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> userOpt = userRepository.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(req.getUsername());
        }

        if (userOpt.isEmpty() || !passwordEncoder.matches(req.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        User user = userOpt.get();
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole(), user.getId());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole(), user.getFullName(), user.getId(), user.getCompanyName(), user.getDepartment(), user.getGpa()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("Unauthorized");
        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        return userOpt.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
