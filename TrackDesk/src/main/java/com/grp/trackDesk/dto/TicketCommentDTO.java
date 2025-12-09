package com.grp.trackDesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketCommentDTO {
	@NotBlank(message = "Content is required")
	private String content;

	@NotNull(message = "Ticket ID is required")
	private Long ticketId;
}
