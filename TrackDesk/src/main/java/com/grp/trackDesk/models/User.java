package com.grp.trackDesk.models;


import org.hibernate.validator.constraints.UniqueElements;

import com.grp.trackDesk.utilities.BaseEntity;
import com.grp.trackDesk.utilities.enumList.UserRole;
import com.grp.trackDesk.utilities.enumList.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TR_USERS")
@Entity
public class User extends BaseEntity {

	@NotBlank
    @Column(name = "username", nullable = false, unique = true)
	private String username;
	@NotBlank
    @Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	@NotBlank
	@Enumerated(EnumType.STRING)
	private UserRole role;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String phone;
	@NotBlank
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	/*
	 * @OneToMany(mappedBy = "user") private List<Address> addresses;
	 * 
	 * @OneToMany(mappedBy = "user") private List<Order> orders;
	 */

}
