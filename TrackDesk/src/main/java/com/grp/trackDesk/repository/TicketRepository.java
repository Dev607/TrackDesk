package com.grp.trackDesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grp.trackDesk.models.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	@Query("SELECT MAX(t.ticketNumber) FROM Ticket t")
    Integer findMaxTicketNumber();
}
