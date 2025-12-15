package com.grp.trackDesk.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
	
	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
	private String username;
	@NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
	private String email;
	@NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	@Pattern(regexp = "^\\+?[0-9\\s\\-]{10,}$", message = "Format de téléphone invalide")
	private String phone;
	private String status;

}
