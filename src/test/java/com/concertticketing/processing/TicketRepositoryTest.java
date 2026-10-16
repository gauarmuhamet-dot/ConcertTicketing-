
package com.concertticketing.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveTicket() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        Ticket ticket = new Ticket(
                "T001", "R001", "A5", 15000.0
        );

        repository.save(ticket);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testFindById() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        Ticket ticket = new Ticket(
                "T002", "R002", "B5", 12000.0
        );

        repository.save(ticket);

        Ticket found = repository.findById("T002");

        assertNotNull(found);
        assertEquals("B5", found.getSeatNumber());
    }

    @Test
    void testFindUnknownTicket() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        assertNull(repository.findById("UNKNOWN"));
    }

    @Test
    void testUpdateExistingTicket() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        Ticket ticket = new Ticket(
                "T003", "R003", "C5", 10000.0
        );

        repository.save(ticket);

        ticket.markUsed();
        repository.save(ticket);

        assertEquals(1, repository.findAll().size());
        assertEquals(Ticket.Status.USED,
                repository.findById("T003").getStatus());
    }

    @Test
    void testRefundedTicketPersistence() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        Ticket ticket = new Ticket(
                "T004", "R004", "A6", 15000.0
        );

        ticket.refund();
        repository.save(ticket);

        assertEquals(Ticket.Status.REFUNDED,
                repository.findById("T004").getStatus());
    }

    @Test
    void testNullTicket() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void testNullFilePath() {
        assertThrows(IllegalArgumentException.class,
                () -> new TicketRepository(null));
    }

    @Test
    void testEmptyRepository() {
        TicketRepository repository =
                new TicketRepository(tempDir.resolve("tickets.csv"));

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testInvalidTicketData() throws Exception {
        Path file = tempDir.resolve("tickets.csv");

        Files.writeString(file, "invalid,data");

        TicketRepository repository =
                new TicketRepository(file);

        assertThrows(IllegalStateException.class,
                repository::findAll);
    }
}
