package com.example.ticket_tracker.ticket;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesAndFindsTicketById() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setTitle("Printer is offline");
        ticket.setDescription("The office printer cannot be reached.");
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);

        // Act
        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
        Long savedId = savedTicket.getId();

        // Remove the saved object from Hibernate's memory so findById
        // must execute a SELECT against PostgreSQL.
        entityManager.clear();

        var result = ticketRepository.findById(savedId);

        // Assert
        assertNotNull(savedId);
        assertTrue(result.isPresent());
        assertEquals("Printer is offline", result.get().getTitle());
        assertEquals(TicketPriority.HIGH, result.get().getPriority());
        assertEquals(TicketStatus.OPEN, result.get().getStatus());

        // This also confirms that PostgreSQL supplied created_at.
        assertNotNull(result.get().getCreatedAt());
    }
}
