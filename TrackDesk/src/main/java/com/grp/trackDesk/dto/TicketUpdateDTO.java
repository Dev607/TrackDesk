package com.grp.trackDesk.dto;

import java.util.Date;

import com.grp.trackDesk.models.User;
import com.grp.trackDesk.utilities.enumList.Priority;
import com.grp.trackDesk.utilities.enumList.RiskLevel;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import lombok.Data;

@Data
public class TicketUpdateDTO {
	
	private String title;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private Long projectId;
    private Long domainId;
    private Date slaDate;
    private RiskLevel risk;
    private User assignedTo;

}
