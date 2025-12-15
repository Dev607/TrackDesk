package com.grp.trackDesk.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import com.grp.trackDesk.dto.AuthResponseDTO;
import com.grp.trackDesk.dto.LoginDTO;
import com.grp.trackDesk.dto.RegisterDTO;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.security.JwtService;
import com.grp.trackDesk.services.UserService;
import com.grp.trackDesk.utilities.enumList.UserRole;
import com.grp.trackDesk.utilities.enumList.UserStatus;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // injecté via configuration
    private final PasswordEncoder passwordEncoder;
    
  
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO dto) {
        try {
            // Vérifier si l'email ou le username existe déjà
            if (userService.existsByEmail(dto.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cet email est déjà utilisé"));
            }
            
            if (userService.existsByUsername(dto.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Ce nom d'utilisateur est déjà utilisé"));
            }
            
           // User user = userService.register(dto, passwordEncoder);
            User user = userService.register(dto);
            // Générer le token après inscription
            String token = jwtService.generateToken(user.getUsername());
            
            AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
            
            log.info("Utilisateur créé avec succès: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Rôle ou statut invalide"));
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Erreur serveur lors de l'inscription"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            String token = jwtService.generateToken(user.getUsername());
            
            AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
            
            log.info("Connexion réussie pour: {}", user.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            log.warn("Tentative de connexion échouée pour: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Email ou mot de passe incorrect"));
        } catch (Exception e) {
            log.error("Erreur lors de la connexion: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Erreur serveur lors de la connexion"));
        }
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Token manquant ou invalide"));
            }
            
            String oldToken = authHeader.substring(7);
            String username = jwtService.extractUsername(oldToken);
            
            if (jwtService.isTokenValid(oldToken, username)) {
                String newToken = jwtService.generateToken(username);
                return ResponseEntity.ok(Map.of("token", newToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token expiré ou invalide"));
            }
            
        } catch (Exception e) {
            log.error("Erreur lors du refresh token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Token invalide"));
        }
    }
    

}
