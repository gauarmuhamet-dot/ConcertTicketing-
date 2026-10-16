
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AccessControlGateTest {

    @TempDir
    Path tempDir;

    private TicketRepository createRepository() {
        return new TicketRepository(tempDir.resolve("tickets.csv"));
    }

    @Test
    void testValidTicketEntry() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T001", "R001", "A5", 15000.0);
        repository.save(ticket);

        AccessControlGate gate = new AccessControlGate(repository);

        assertTrue(gate.allowEntry("T001"));
        assertEquals(Ticket.Status.USED,
                repository.findById("T001").getStatus());
    }

    @Test
    void testDoubleEntry() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T002", "R002", "B5", 12000.0);
        repository.save(ticket);

        AccessControlGate gate = new AccessControlGate(repository);

        assertTrue(gate.allowEntry("T002"));
        assertFalse(gate.allowEntry("T002"));
    }

    @Test
    void testUnknownTicket() {
        TicketRepository repository = createRepository();
        AccessControlGate gate = new AccessControlGate(repository);

        assertFalse(gate.allowEntry("UNKNOWN"));
    }

    @Test
    void testRefundedTicket() {
        TicketRepository repository = createRepository();
        Ticket ticket = new Ticket("T003", "R003", "C5", 10000.0);
        ticket.refund();
        repository.save(ticket);

        AccessControlGate gate = new AccessControlGate(repository);

        assertFalse(gate.allowEntry("T003"));
    }

    @Test
    void testNullAndEmptyTicketId() {
        AccessControlGate gate =
                new AccessControlGate(createRepository());

        assertFalse(gate.allowEntry(null));
        assertFalse(gate.allowEntry(""));
        assertFalse(gate.allowEntry("   "));
    }

    @Test
    void testNullRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> new AccessControlGate(null));
    }
}
