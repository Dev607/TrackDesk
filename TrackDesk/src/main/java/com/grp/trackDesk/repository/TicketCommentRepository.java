package com.grp.trackDesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grp.trackDesk.models.TicketComment;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long>{
	List<TicketComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
