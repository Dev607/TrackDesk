package com.grp.trackDesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

	    private String token;
	    private String tokenType = "Bearer";
	    private Long userId;
	    private String email;
	    private String username;
	    private String role;
	    private String firstName;
	    private String lastName;
	}

