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
	    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketDTO dto,
	    		@RequestHeader("X-User-Email") String creatorEmail) {
	        Ticket created = ticketService.createTicket(dto, creatorEmail);
	        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
	        return ResponseEntity.ok(ticketService.getTicketById(id));
	    }

	    @GetMapping
	    public ResponseEntity<List<Ticket>> getAllTickets() {
	        return ResponseEntity.ok(ticketService.getAllTickets());
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<Ticket> updateTicket(
	            @PathVariable Long id,
	            @Valid @RequestBody TicketDTO dto
	    ) {
	        Ticket updated = ticketService.updateTicket(id, dto);
	        return ResponseEntity.ok(updated);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
	        ticketService.deleteTicket(id);
	        return ResponseEntity.noContent().build();
	    }

}
