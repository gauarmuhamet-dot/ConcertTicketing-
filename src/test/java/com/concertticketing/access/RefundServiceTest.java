
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RefundServiceTest {

    @TempDir
    Path tempDir;

    private TicketRepository createRepository() {
        return new TicketRepository(tempDir.resolve("tickets.csv"));
    }

    @Test
    void testRefundUnusedTicket() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T001", "R001", "A5", 15000.0);
        repository.save(ticket);

        RefundService service = new RefundService(repository);

        assertTrue(service.refundTicket("T001"));
        assertEquals(Ticket.Status.REFUNDED,
                repository.findById("T001").getStatus());
    }

    @Test
    void testRefundUsedTicket() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T002", "R002", "B5", 12000.0);
        ticket.markUsed();
        repository.save(ticket);

        RefundService service = new RefundService(repository);

        assertFalse(service.refundTicket("T002"));
    }

    @Test
    void testRefundUnknownTicket() {
        TicketRepository repository = createRepository();
        RefundService service = new RefundService(repository);

        assertFalse(service.refundTicket("UNKNOWN"));
    }

    @Test
    void testDoubleRefund() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T003", "R003", "C5", 10000.0);
        repository.save(ticket);

        RefundService service = new RefundService(repository);

        assertTrue(service.refundTicket("T003"));
        assertFalse(service.refundTicket("T003"));
    }

    @Test
    void testNullTicketId() {
        RefundService service = new RefundService(createRepository());

        assertFalse(service.refundTicket(null));
    }

    @Test
    void testNullRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> new RefundService(null));
    }
}
