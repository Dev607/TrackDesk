package com.grp.trackDesk.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grp.trackDesk.exception.DuplicateResourceException;
import com.grp.trackDesk.exception.ResourceNotFoundException;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.repository.UserRepository;
import com.grp.trackDesk.utilities.enumList.UserStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	private final UserRepository userRepo;
	   private final PasswordEncoder passwordEncoder;
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public User getUserById(Long id) {
		return userRepo.findById(id)
				// .orElseThrow(() -> new RuntimeException("User not found"));
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
	}

	public User saveUser(User user) {
		try {
			if (userRepo.existsByUsername(user.getUsername())) {
				throw new DuplicateResourceException("User", "name", user.getUsername());
			}

		} catch (DataIntegrityViolationException ex) {
			log.error("Data integrity violation while creating user: {}", ex.getMessage());
			throw new DuplicateResourceException("User", "name", user.getUsername());
			// TODO: handle exception
		}
		return userRepo.save(user);
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}
	
	  @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        User user = userRepo.findByEmail(email)
	                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        return new org.springframework.security.core.userdetails.User(
	            user.getEmail(),
	            user.getPassword(),
	            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
	        );
	    }

	  public User register(User user, String rawPassword, PasswordEncoder passwordEncoder) {

		    // Encoder le mot de passe
		    String encodedPassword = passwordEncoder.encode(rawPassword);
		    user.setPassword(encodedPassword);

		    // Définir un statut par défaut si nécessaire
		    if (user.getStatus() == null) {
		        user.setStatus(UserStatus.ENABLED);
		    }

		    // Sauvegarder
		    return userRepo.save(user);
		}
}
