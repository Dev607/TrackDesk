package com.grp.trackDesk.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grp.trackDesk.dto.RegisterDTO;
import com.grp.trackDesk.exception.BusinessException;
import com.grp.trackDesk.exception.DuplicateResourceException;
import com.grp.trackDesk.exception.ResourceNotFoundException;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.repository.UserRepository;
import com.grp.trackDesk.utilities.enumList.UserRole;
import com.grp.trackDesk.utilities.enumList.UserStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	    /**
	     * Récupère tous les utilisateurs
	     */
	    public List<User> getAllUsers() {
	        log.debug("Récupération de tous les utilisateurs");
	        return userRepository.findAll();
	    }

	    /**
	     * Récupère un utilisateur par son ID
	     */
	    public User getUserById(Long id) {
	        log.debug("Récupération de l'utilisateur avec ID: {}", id);
	        return userRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
	    }

	    /**
	     * Récupère un utilisateur par son email
	     */
	    public Optional<User> findByEmail(String email) {
	        log.debug("Recherche de l'utilisateur avec email: {}", email);
	        return userRepository.findByEmail(email);
	    }

	    
	    /**
	     * Récupère un utilisateur par son nom d'utilisateur
	     */
	    public Optional<User> findByUsername(String username) {
	        log.debug("Recherche de l'utilisateur avec username: {}", username);
	        return userRepository.findByUsername(username);
	    }

	    
	    /**
	     * Vérifie si un email existe déjà
	     */
	    public boolean existsByEmail(String email) {
	        return userRepository.existsByEmail(email);
	    }

	    /**
	     * Vérifie si un nom d'utilisateur existe déjà
	     */
	    public boolean existsByUsername(String username) {
	        return userRepository.existsByUsername(username);
	    }

	    /**
	     * Crée un nouvel utilisateur avec validation des doublons
	     */
	    @Transactional
	    public User saveUser(User user) {
	        log.debug("Création d'un nouvel utilisateur: {}", user.getEmail());
	        
	        // Vérification des doublons
	        if (user.getId() == null) {
	            validateUserUniqueness(user);
	        }
	        
	        // Validation des données
	        validateUserData(user);
	        
	        try {
	            return userRepository.save(user);
	        } catch (DataIntegrityViolationException ex) {
	            log.error("Violation d'intégrité des données lors de la création: {}", ex.getMessage());
	            throw new DuplicateResourceException("Utilisateur", "email ou username", user.getEmail());
	        }
	    }

	    /**
	     * Met à jour un utilisateur existant
	     */
	    @Transactional
	    public User updateUser(Long id, User userDetails) {
	        log.debug("Mise à jour de l'utilisateur avec ID: {}", id);
	        
	        User existingUser = getUserById(id);
	        
	        // Mise à jour des champs
	        if (userDetails.getFirstName() != null) {
	            existingUser.setFirstName(userDetails.getFirstName());
	        }
	        if (userDetails.getLastName() != null) {
	            existingUser.setLastName(userDetails.getLastName());
	        }
	        if (userDetails.getPhone() != null) {
	            existingUser.setPhone(userDetails.getPhone());
	        }
	        if (userDetails.getStatus() != null) {
	            existingUser.setStatus(userDetails.getStatus());
	        }
	        if (userDetails.getRole() != null) {
	            existingUser.setRole(userDetails.getRole());
	        }
	        
	        // Vérifier l'unicité si email ou username changent
	        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(existingUser.getEmail())) {
	            if (existsByEmail(userDetails.getEmail())) {
	                throw new DuplicateResourceException("Utilisateur", "email", userDetails.getEmail());
	            }
	            existingUser.setEmail(userDetails.getEmail());
	        }
	        
	        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(existingUser.getUsername())) {
	            if (existsByUsername(userDetails.getUsername())) {
	                throw new DuplicateResourceException("Utilisateur", "username", userDetails.getUsername());
	            }
	            existingUser.setUsername(userDetails.getUsername());
	        }
	        
	        return userRepository.save(existingUser);
	    }

	    /**
	     * Supprime un utilisateur
	     */
	    @Transactional
	    public void deleteUser(Long id) {
	        log.debug("Suppression de l'utilisateur avec ID: {}", id);
	        
	        if (!userRepository.existsById(id)) {
	            throw new ResourceNotFoundException("Utilisateur", "id", id);
	        }
	        
	        userRepository.deleteById(id);
	        log.info("Utilisateur supprimé avec succès. ID: {}", id);
	    }

	    /**
	     * Charge un utilisateur pour l'authentification Spring Security
	     */
	    @Override

	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        log.debug("Chargement de l'utilisateur pour l'authentification: {}", username);
	        
	        // Chercher par email (ou ajouter une méthode dans le repository pour chercher par email ou username)
	        User user = userRepository.findByEmail(username)
	                .orElseThrow(() -> {
	                    log.warn("Tentative de connexion avec identifiant inconnu: {}", username);
	                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant: " + username);
	                });
	        
	        // Vérifier si le compte est actif
	        if (!UserStatus.ENABLED.equals(user.getStatus())) {
	            log.warn("Tentative de connexion avec un compte désactivé: {}", username);
	            throw new BusinessException("Votre compte est désactivé. Veuillez contacter l'administrateur.");
	        }
	        
	        return new org.springframework.security.core.userdetails.User(
	                user.getEmail(),
	                user.getPassword(),
	                user.getStatus().equals(UserStatus.ENABLED),
	                true, // account non expiré
	                true, // credentials non expirés
	                true, // account non verrouillé
	                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
	        );
	    }
	    /**
	     * Inscription d'un nouvel utilisateur
	     */
	    @Transactional
	    public User register(User user, String rawPassword) {
	        log.debug("Inscription d'un nouvel utilisateur: {}", user.getEmail());
	        
	        // Vérifier les doublons
	        validateUserUniqueness(user);
	        
	        // Encoder le mot de passe
	        user.setPassword(passwordEncoder.encode(rawPassword));
	        
	        // Définir des valeurs par défaut si nécessaire
	        if (user.getRole() == null) {
	            user.setRole(UserRole.CUSTOMER);
	        }
	        
	        if (user.getStatus() == null) {
	            user.setStatus(UserStatus.DESABLED);
	        }
	        
	        // Validation des données
	        validateUserData(user);
	        
	        try {
	            User savedUser = userRepository.save(user);
	            log.info("Utilisateur inscrit avec succès: {}", savedUser.getEmail());
	            return savedUser;
	        } catch (DataIntegrityViolationException ex) {
	            log.error("Erreur d'intégrité lors de l'inscription: {}", ex.getMessage());
	            throw new DuplicateResourceException("Utilisateur", "email ou username", user.getEmail());
	        }
	    }

	    /**
	     * Inscription à partir d'un DTO
	     */
	    @Transactional
	    public User register(RegisterDTO dto) {
	        log.debug("Inscription à partir du DTO pour: {}", dto.getEmail());
	        
	        // Vérifier les doublons
	        if (existsByEmail(dto.getEmail())) {
	            throw new DuplicateResourceException("Utilisateur", "email", dto.getEmail());
	        }
	        
	        if (existsByUsername(dto.getUsername())) {
	            throw new DuplicateResourceException("Utilisateur", "username", dto.getUsername());
	        }
	        
	        // Créer l'utilisateur
	        User user = new User();
	        user.setUsername(dto.getUsername());
	        user.setEmail(dto.getEmail());
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	        user.setFirstName(dto.getFirstName());
	        user.setLastName(dto.getLastName());
	        user.setPhone(dto.getPhone());
	        
	        // Gérer le rôle
	        try {
	            user.setRole(UserRole.valueOf(dto.getRole()));
	        } catch (IllegalArgumentException e) {
	            log.warn("Rôle invalide '{}', utilisation du rôle par défaut USER", dto.getRole());
	            user.setRole(UserRole.CUSTOMER);
	        }
	        
	        // Gérer le statut
	        try {
	            user.setStatus(UserStatus.valueOf(dto.getStatus()));
	        } catch (IllegalArgumentException e) {
	            log.warn("Statut invalide '{}', utilisation du statut par défaut PENDING", dto.getStatus());
	            user.setStatus(UserStatus.PENDING);
	        }
	        
	        // Valider les données
	        validateUserData(user);
	        
	        try {
	            User savedUser = userRepository.save(user);
	            log.info("Utilisateur créé avec succès à partir du DTO: {}", savedUser.getEmail());
	            return savedUser;
	        } catch (DataIntegrityViolationException ex) {
	            log.error("Erreur d'intégrité lors de l'inscription DTO: {}", ex.getMessage());
	            throw new DuplicateResourceException("Utilisateur", "email ou username", dto.getEmail());
	        }
	    }

	    /**
	     * Active un compte utilisateur
	     */
	    @Transactional
	    public User activateUser(Long userId) {
	        log.debug("Activation de l'utilisateur avec ID: {}", userId);
	        
	        User user = getUserById(userId);
	        user.setStatus(UserStatus.ENABLED);
	        
	        User activatedUser = userRepository.save(user);
	        log.info("Utilisateur activé: {}", activatedUser.getEmail());
	        
	        return activatedUser;
	    }

	    /**
	     * Désactive un compte utilisateur
	     */
	    @Transactional
	    public User deactivateUser(Long userId) {
	        log.debug("Désactivation de l'utilisateur avec ID: {}", userId);
	        
	        User user = getUserById(userId);
	        user.setStatus(UserStatus.PENDING);
	        
	        User deactivatedUser = userRepository.save(user);
	        log.info("Utilisateur désactivé: {}", deactivatedUser.getEmail());
	        
	        return deactivatedUser;
	    }

	    /**
	     * Change le mot de passe d'un utilisateur
	     */
	    @Transactional
	    public void changePassword(Long userId, String currentPassword, String newPassword) {
	        log.debug("Changement de mot de passe pour l'utilisateur ID: {}", userId);
	        
	        User user = getUserById(userId);
	        
	        // Vérifier l'ancien mot de passe
	        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	            throw new BusinessException("Le mot de passe actuel est incorrect");
	        }
	        
	        // Encoder le nouveau mot de passe
	        user.setPassword(passwordEncoder.encode(newPassword));
	        
	        userRepository.save(user);
	        log.info("Mot de passe changé pour l'utilisateur: {}", user.getEmail());
	    }

	    /**
	     * Réinitialise le mot de passe (par admin)
	     */
	    @Transactional
	    public void resetPassword(Long userId, String newPassword) {
	        log.debug("Réinitialisation du mot de passe pour l'utilisateur ID: {}", userId);
	        
	        User user = getUserById(userId);
	        user.setPassword(passwordEncoder.encode(newPassword));
	        
	        userRepository.save(user);
	        log.info("Mot de passe réinitialisé pour l'utilisateur: {}", user.getEmail());
	    }

	    /**
	     * Valide l'unicité des données utilisateur
	     */
	    private void validateUserUniqueness(User user) {
	        if (existsByEmail(user.getEmail())) {
	            throw new DuplicateResourceException("Utilisateur", "email", user.getEmail());
	        }
	        
	        if (existsByUsername(user.getUsername())) {
	            throw new DuplicateResourceException("Utilisateur", "username", user.getUsername());
	        }
	    }

	    /**
	     * Valide les données de l'utilisateur
	     */
	    private void validateUserData(User user) {
	        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
	            throw new BusinessException("L'email est obligatoire");
	        }
	        
	        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
	            throw new BusinessException("Le nom d'utilisateur est obligatoire");
	        }
	        
	        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
	            throw new BusinessException("Le mot de passe est obligatoire");
	        }
	        
	        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
	            throw new BusinessException("Format d'email invalide");
	        }
	    }

	    /**
	     * Recherche des utilisateurs par statut
	     */
	    public List<User> findUsersByStatus(UserStatus status) {
	        log.debug("Recherche des utilisateurs avec statut: {}", status);
	        return userRepository.findByStatus(status);
	        
	    }

	    /**
	     * Recherche des utilisateurs par rôle
	     */
	    public List<User> findUsersByRole(UserRole role) {
	        log.debug("Recherche des utilisateurs avec rôle: {}", role);
	        return userRepository.findByRole(role);
	    }

	    /**
	     * Vérifie si un utilisateur a un rôle spécifique
	     */
	    public boolean hasRole(Long userId, UserRole role) {
	        User user = getUserById(userId);
	        return user.getRole().equals(role);
	    }
}
