
package com.concertticketing.processing;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDemoTest {

    @Test
    void testTicketDemo() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            TicketDemo.main(new String[0]);

        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("=== TICKET INFORMATION ==="));
        assertTrue(result.contains("T001"));
        assertTrue(result.contains("Fraud check: true"));
        assertTrue(result.contains("Ticket saved successfully!"));

        TicketRepository repository = new TicketRepository(
                Path.of("data", "tickets.csv"));

        Ticket ticket = repository.findById("T001");

        assertNotNull(ticket);
        assertEquals("R001", ticket.getReservationId());
        assertEquals("A5", ticket.getSeatNumber());
        assertEquals(15000.0, ticket.getPrice(), 0.001);

        assertTrue(Files.isDirectory(
                Path.of("data", "printed_tickets")));
    }
}
