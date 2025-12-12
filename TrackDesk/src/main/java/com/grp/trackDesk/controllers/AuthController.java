package com.grp.trackDesk.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grp.trackDesk.dto.LoginDTO;
import com.grp.trackDesk.dto.RegisterDTO;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.security.JwtService;
import com.grp.trackDesk.services.UserService;
import com.grp.trackDesk.utilities.enumList.UserRole;
import com.grp.trackDesk.utilities.enumList.UserStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // injecté via configuration
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());

        
        user.setRole(UserRole.valueOf(dto.getRole()));     // ✔ OK
        user.setStatus(UserStatus.valueOf(dto.getStatus())); // ✔ OK

        userService.register(user, dto.getPassword(), passwordEncoder);
        return ResponseEntity.ok(Map.of("message", "user created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {

        // Authenticate (throws Exception if bad)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Générer token
        String token = jwtService.generateToken(dto.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
