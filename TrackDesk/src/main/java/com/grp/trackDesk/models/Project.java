package com.grp.trackDesk.models;

import com.grp.trackDesk.utilities.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TR_PROJECT")
@Entity
public class Project extends BaseEntity{

	
	@NotBlank(message = "Le nom est obligatoire")
	private String name;
	@NotBlank(message = "La description est obligatoire")
	@Size(max = 255)
	private String description;
	
}
