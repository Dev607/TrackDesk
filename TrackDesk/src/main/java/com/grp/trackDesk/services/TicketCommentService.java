package com.grp.trackDesk.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grp.trackDesk.dto.TicketCommentDTO;
import com.grp.trackDesk.models.Ticket;
import com.grp.trackDesk.models.TicketComment;
import com.grp.trackDesk.models.User;
import com.grp.trackDesk.repository.TicketCommentRepository;
import com.grp.trackDesk.repository.TicketRepository;
import com.grp.trackDesk.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketCommentService {

	
	private final TicketCommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    
    
    public TicketComment addComment(TicketCommentDTO dto, String userEmail) {

        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TicketComment comment = new TicketComment();
        comment.setContent(dto.getContent());
        comment.setTicket(ticket);
        comment.setCreatedBy(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }
    
    
    public List<TicketComment> getCommentsByTicket(Long ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
