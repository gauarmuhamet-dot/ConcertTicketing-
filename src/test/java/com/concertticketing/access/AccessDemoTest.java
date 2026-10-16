
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AccessDemoTest {

    @Test
    void testAccessDemo() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true,
                    StandardCharsets.UTF_8));

            AccessDemo.main(new String[0]);

        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("First entry: true"));
        assertTrue(result.contains("Second entry: false"));
        assertTrue(result.contains("Refund unused ticket: true"));
        assertTrue(result.contains("Entry after refund: false"));
        assertTrue(result.contains("Refund used ticket: false"));
        assertTrue(result.contains("Unknown ticket entry: false"));

        TicketRepository repository = new TicketRepository(
                Path.of("data", "access_test_tickets.csv")
        );

        assertEquals(Ticket.Status.USED,
                repository.findById("TEST001").getStatus());

        assertEquals(Ticket.Status.REFUNDED,
                repository.findById("TEST002").getStatus());
    }
}
