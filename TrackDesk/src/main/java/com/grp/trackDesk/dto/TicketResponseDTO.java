package com.grp.trackDesk.dto;

import java.time.LocalDateTime;

import com.grp.trackDesk.utilities.enumList.Priority;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import lombok.Data;

@Data
public class TicketResponseDTO {
	private Long id;
	private String name;
	private String description;
	private Integer ticketNumber;
	private Priority priority;
	private TicketStatus status;

	private Long projectId;
	private String projectName;

	private Long createdById;
	private String createdByUsername;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
