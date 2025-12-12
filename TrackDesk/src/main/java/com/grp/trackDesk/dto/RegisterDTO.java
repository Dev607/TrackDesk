package com.grp.trackDesk.dto;

import lombok.Data;

@Data
public class RegisterDTO {
	private String username;
	private String email;
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	private String phone;
	private String status;

}
