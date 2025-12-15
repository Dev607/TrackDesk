package com.grp.trackDesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grp.trackDesk.models.User;
import com.grp.trackDesk.utilities.enumList.UserRole;
import com.grp.trackDesk.utilities.enumList.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//Optional <User>existsByUsername(String username);
	boolean existsByUsername(String username);

	boolean existsByEmail(String username);

	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String userName);

	List<User> findByStatus(UserStatus status);

	List<User> findByRole(UserRole role);
	// Optional<User> findByUsername(String username);

}
