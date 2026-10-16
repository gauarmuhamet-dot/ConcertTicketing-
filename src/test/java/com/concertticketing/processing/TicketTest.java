
package com.concertticketing.processing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    void testNewTicket() {
        Ticket ticket = new Ticket(
                "T001", "R001", "A5", 15000.0
        );

        assertEquals("T001", ticket.getTicketId());
        assertEquals("R001", ticket.getReservationId());
        assertEquals("A5", ticket.getSeatNumber());
        assertEquals(15000.0, ticket.getPrice());
        assertEquals(Ticket.Status.ISSUED, ticket.getStatus());
    }

    @Test
    void testMarkUsed() {
        Ticket ticket = new Ticket(
                "T002", "R002", "B5", 12000.0
        );

        ticket.markUsed();

        assertEquals(Ticket.Status.USED, ticket.getStatus());
    }

    @Test
    void testRefundTicket() {
        Ticket ticket = new Ticket(
                "T003", "R003", "C5", 10000.0
        );

        ticket.refund();

        assertEquals(Ticket.Status.REFUNDED, ticket.getStatus());
    }

    @Test
    void testRefundUsedTicket() {
        Ticket ticket = new Ticket(
                "T004", "R004", "A6", 15000.0
        );

        ticket.markUsed();

        assertThrows(IllegalStateException.class,
                ticket::refund);
    }

    @Test
    void testInvalidPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Ticket(
                        "T005", "R005", "A7", -100.0
                ));
    }
}
