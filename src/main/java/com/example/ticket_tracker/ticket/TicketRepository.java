package com.example.ticket_tracker.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {
}
