package com.grp.trackDesk.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.grp.trackDesk.exception.DuplicateResourceException;
import com.grp.trackDesk.exception.ResourceNotFoundException;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;

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
}
