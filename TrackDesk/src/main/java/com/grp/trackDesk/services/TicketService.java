package com.grp.trackDesk.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grp.trackDesk.dto.TicketDTO;
import com.grp.trackDesk.models.Project;
import com.grp.trackDesk.models.Ticket;
import com.grp.trackDesk.models.TrDomain;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.repository.DomainRepository;
import com.grp.trackDesk.repository.ProjectRepository;
import com.grp.trackDesk.repository.TicketRepository;
import com.grp.trackDesk.repository.UserRepository;
import com.grp.trackDesk.utilities.enumList.TicketStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final DomainRepository domainRepository;

	/**
	 * Create a new ticket
	 */
	
	 @Transactional
	public Ticket createTicket(TicketDTO dto, String creatorEmail) {
		 
		 Ticket ticket = new Ticket();
		 
		 ticket.setName(dto.getTitle());
	        ticket.setDescription(dto.getDescription());
	        ticket.setPriority(dto.getPriority());
	        ticket.setStatus(TicketStatus.NEW); // statut initial
	        ticket.setCreatedAt(LocalDateTime.now());
	        ticket.setUpdatedAt(LocalDateTime.now());
	        
	     // Assign creator
	        User creator = userRepository.findByEmail(creatorEmail)
	                .orElseThrow(() -> new RuntimeException("Creator not found"));
	        ticket.setCreatedBy(creator);
	        
	     // Assign project
	        Project project = projectRepository.findById(dto.getProjectId())
	                .orElseThrow(() -> new RuntimeException("Project not found"));
	        ticket.setProject(project);
	        
	        // Domain and other fields optional
	        if (dto.getDomainId() != null) {
	            TrDomain domain = domainRepository.findById(dto.getDomainId())
	                    .orElseThrow(() -> new RuntimeException("Domain not found"));
	            ticket.setDomain(domain);
	        }

	        return ticketRepository.save(ticket);

		
	}

	/**
	 * Update a ticket
	 */
	 
	 @Transactional
	public Ticket updateTicket(Long id, TicketDTO dto) {
		
		Ticket ticket = getTicketById(id);

		if (dto.getTitle() != null) ticket.setName(dto.getTitle());
        if (dto.getDescription() != null) ticket.setDescription(dto.getDescription());
        if (dto.getPriority() != null) ticket.setPriority(dto.getPriority());
        if (dto.getStatus() != null) ticket.setStatus(dto.getStatus());
        
        
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            ticket.setProject(project);
        }

        if (dto.getDomainId() != null) {
            TrDomain domain = domainRepository.findById(dto.getDomainId())
                    .orElseThrow(() -> new RuntimeException("Domain not found"));
            ticket.setDomain(domain);
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        
        return ticketRepository.save(ticket);


	}

	/**
	 * Change status with business rules
	 */
	public Ticket changeStatus(Long id, TicketStatus newStatus) {
		Ticket tk = getTicketById(id);

		validateStatusTransition(tk.getStatus(), newStatus);

		tk.setStatus(newStatus);
		return ticketRepository.save(tk);
	}

	  /**
     * Get a ticket by id
     */
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

	/**
	 * List all tickets
	 */
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	/**
	 * Business rule: Allowed transitions
	 */
	private void validateStatusTransition(TicketStatus oldStatus, TicketStatus newStatus) {

		switch (oldStatus) {
		case NEW -> {
			if (newStatus != TicketStatus.OPEN && newStatus != TicketStatus.ASSIGNED) {
				throw new IllegalArgumentException("NEW can only go to OPEN or ASSIGNED");
			}
		}
		case OPEN -> {
			if (newStatus == TicketStatus.CLOSED) {
				throw new IllegalArgumentException("Cannot close OPEN directly");
			}
		}
		case RESOLVED -> {
			if (newStatus != TicketStatus.CLOSED && newStatus != TicketStatus.REOPENED) {
				throw new IllegalArgumentException("RESOLVED → CLOSED or REOPENED only");
			}
		}
		}
	}

	/**
	 * Validation of relations (domain, project, users)
	 */
	private void validateRelations(Ticket t) {

		if (t.getProject() == null || t.getProject().getId() == null) {
			throw new RuntimeException("Project is required");
		}

		if (!projectRepository.existsById(t.getProject().getId())) {
			throw new RuntimeException("Project does not exist");
		}

		if (t.getDomain() == null || t.getDomain().getId() == null) {
			throw new RuntimeException("Domain is required");
		}

		if (!domainRepository.existsById(t.getDomain().getId())) {
			throw new RuntimeException("Domain does not exist");
		}

		if (t.getCreatedBy() == null || t.getCreatedBy().getId() == null) {
			throw new RuntimeException("CreatedBy is required");
		}

		if (!userRepository.existsById(t.getCreatedBy().getId())) {
			throw new RuntimeException("CreatedBy user does not exist");
		}
	}

	/**
	 * Generate next ticket number
	 */
	private Integer generateNextTicketNumber() {
		Integer max = ticketRepository.findMaxTicketNumber();
		return (max == null) ? 1 : max + 1;
	}
	
	@Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }

}
