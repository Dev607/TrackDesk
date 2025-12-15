package com.grp.trackDesk.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.grp.trackDesk.exception.ApiError;
import com.grp.trackDesk.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final UserService userService; // impl de UserDetailsService
	 private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String token;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		 token = authHeader.substring(7);
		try {
			username = jwtService.extractUsername(token);
			System.out.println("username == "+ username);
			 if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				 System.out.println("haithem dans le if");
	                UserDetails userDetails = userService.loadUserByUsername(username);
	                
	                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
	                    UsernamePasswordAuthenticationToken authToken = 
	                        new UsernamePasswordAuthenticationToken(
	                            userDetails, 
	                            null, 
	                            userDetails.getAuthorities()
	                        );
	                    authToken.setDetails(
	                        new WebAuthenticationDetailsSource().buildDetails(request)
	                    );
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                    log.debug("Utilisateur authentifié: {}", username);
	                } else {
	                    log.warn("Token invalide pour l'utilisateur: {}", username);
	                }
	            }
	            
	        } catch (Exception e) {
	            log.error("Erreur lors de l'authentification JWT: {}", e.getMessage());
	            
	            // Réponse d'erreur structurée
	            ApiError error = new ApiError(
	                HttpStatus.UNAUTHORIZED,
	                "Token d'authentification invalide ou expiré"
	            );
	            error.setPath(request.getRequestURI());
	            
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.setContentType("application/json");
	            response.getWriter().write(objectMapper.writeValueAsString(error));
	            return;
    }
        
        filterChain.doFilter(request, response);
}
	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || 
               path.startsWith("/actuator/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs");
    }
}