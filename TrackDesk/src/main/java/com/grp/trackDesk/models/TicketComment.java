package com.grp.trackDesk.models;

import java.util.Date;

import com.grp.trackDesk.utilities.BaseEntity;
import com.grp.trackDesk.utilities.enumList.Priority;
import com.grp.trackDesk.utilities.enumList.RiskLevel;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketComment extends BaseEntity {

	@Column(nullable = false, length = 2000)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

}
