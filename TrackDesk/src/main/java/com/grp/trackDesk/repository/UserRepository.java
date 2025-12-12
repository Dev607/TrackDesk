package com.grp.trackDesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.grp.trackDesk.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByUsername(String username);
	Optional<User> findByEmail(String email);
	//Optional<User> findByUsername(String username);

}
