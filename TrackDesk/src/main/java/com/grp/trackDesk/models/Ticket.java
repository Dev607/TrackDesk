package com.grp.trackDesk.models;

import java.util.Date;

import org.hibernate.validator.constraints.UniqueElements;

import com.grp.trackDesk.utilities.BaseEntity;
import com.grp.trackDesk.utilities.enumList.Priority;
import com.grp.trackDesk.utilities.enumList.RiskLevel;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TR_TICKET")
@Entity
public class Ticket extends BaseEntity {

	@NotBlank(message = "Le nom est obligatoire")
	private String name;

	@NotBlank(message = "La description est obligatoire")
	@Size(max = 255)
	private String description;

	@NotNull(message = "Le numéro du ticket est obligatoire")
	@Column(unique = true, nullable = false)
	private Integer ticketNumber;

	private Date slaDate;

	@Enumerated(EnumType.STRING)
	private Priority priority; // 1, 2, 3, 4, 5 enum

	@Enumerated(EnumType.STRING)
	private TicketStatus status; // New, Open, Assigned, In Progress, On Hold, Resolved, Closed, Reopened

	@ManyToOne
	@JoinColumn(name = "domain_id")
	private TrDomain domain; // payment, tradeFinance a prevoir une table

	@Enumerated(EnumType.STRING)
	private RiskLevel risk; // high, low, medieum

	@NotNull(message = "Le projet est obligatoire")
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	@NotNull(message = "Créé par est obligatoire")
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "assigned_to")
	private User assignedTo;

	@Size(max = 255)
	private String tkComment;

	// filejoined

}
