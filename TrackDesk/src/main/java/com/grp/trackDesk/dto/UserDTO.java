package com.grp.trackDesk.dto;



import com.grp.trackDesk.models.User;
import com.grp.trackDesk.utilities.enumList.UserRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	private String username;
	private String email;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	private String firstName;
	private String lastName;
	private String phone;
	
	
	public static UserDTO toDto(User entity) {
		if (entity == null)
			return null;
		return UserDTO.builder().id(entity.getId()).username(entity.getUsername()).email(entity.getEmail())
				.role(entity.getRole()).firstName(entity.getFirstName()).lastName(entity.getLastName())
				.phone(entity.getPhone()).build();
	}
	
	 public static User toEntity(UserDTO dto ) {
	        if (dto == null) return null;
	        User user = new User();
	        user.setId(dto.getId());
	        user.setUsername(dto.getUsername());
	        user.setEmail(dto.getEmail());
	        user.setRole(dto.getRole());
	        user.setFirstName(dto.getFirstName());
	        user.setLastName(dto.getLastName());
	        user.setPhone(dto.getPhone());
	        return user;
	    }


}
