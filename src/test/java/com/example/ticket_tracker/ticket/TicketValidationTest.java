package com.example.ticket_tracker.ticket;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void blankTitleIsRejected() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setTitle("   ");
        ticket.setDescription("Example description");
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setStatus(TicketStatus.OPEN);

        // Act
        Set<ConstraintViolation<Ticket>> violations =
                validator.validate(ticket);

        // Assert
        boolean hasTitleViolation = violations.stream()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("title"));

        assertTrue(hasTitleViolation);
    }
}
