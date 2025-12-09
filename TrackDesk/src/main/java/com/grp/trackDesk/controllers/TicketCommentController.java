package com.grp.trackDesk.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grp.trackDesk.dto.TicketCommentDTO;
import com.grp.trackDesk.models.TicketComment;
import com.grp.trackDesk.services.TicketCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/api/ticket-comments")
@RequiredArgsConstructor
public class TicketCommentController {
	private final TicketCommentService commentService;

	@PostMapping
	public ResponseEntity<TicketComment> addComment(@Valid @RequestBody TicketCommentDTO dto,
			@RequestHeader("X-User-Email") String userEmail) {

		TicketComment created = commentService.addComment(dto, userEmail);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping("/ticket/{ticketId}")
	public ResponseEntity<List<TicketComment>> getCommentsByTicket(@PathVariable Long ticketId) {
		return ResponseEntity.ok(commentService.getCommentsByTicket(ticketId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
		commentService.deleteComment(id);
		return ResponseEntity.noContent().build();
	}
}
