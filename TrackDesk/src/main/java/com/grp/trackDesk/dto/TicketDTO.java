package com.grp.trackDesk.dto;


import com.grp.trackDesk.utilities.enumList.Priority;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketDTO {
	
	@NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Status is required")
    private TicketStatus status;

    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    private Long domainId; // optional
}
