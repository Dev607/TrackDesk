package com.grp.trackDesk.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grp.trackDesk.dto.TicketDTO;
import com.grp.trackDesk.dto.TicketResponseDTO;
import com.grp.trackDesk.dto.TicketUpdateDTO;
import com.grp.trackDesk.models.Ticket;
import com.grp.trackDesk.services.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
	 private final TicketService ticketService;

	    @PostMapping
	    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketDTO dto,
	    		@RequestHeader("X-User-Email") String creatorEmail) {
	        Ticket created = ticketService.createTicket(dto, creatorEmail);
	        return ResponseEntity
	        		.status(HttpStatus.CREATED)
	        		.body(ticketService.toResponseDTO(created));
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long id) {
	    	Ticket ticket = ticketService.getTicketById(id);
	        return ResponseEntity.ok(ticketService.toResponseDTO(ticket));
	    }

	    @GetMapping
	    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
	    	 List<TicketResponseDTO> list = ticketService.getAllTickets()
	                 .stream()
	                 .map(ticketService::toResponseDTO)
	                 .toList();
	         return ResponseEntity.ok(list);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<TicketResponseDTO> updateTicket(
	            @PathVariable Long id,
	            @RequestBody TicketUpdateDTO dto
	    ) {
	        Ticket updated = ticketService.updateTicket(id, dto);
	        return ResponseEntity.status(HttpStatus.OK)
	        		.body(ticketService.toResponseDTO(updated));
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
	        ticketService.deleteTicket(id);
	        return ResponseEntity.noContent().build();
	    }

}
