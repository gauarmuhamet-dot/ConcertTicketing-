
package com.concertticketing.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TicketPrinterTest {

    @Test
    void testPrintTicket() {
        Ticket ticket = new Ticket(
                "T001", "R001", "A5", 15000.0
        );

        TicketPrinter printer = new TicketPrinter();
        String result = printer.printTicket(ticket);

        assertTrue(result.contains("CONCERT TICKET"));
        assertTrue(result.contains("T001"));
        assertTrue(result.contains("R001"));
        assertTrue(result.contains("A5"));
        assertTrue(result.contains("15000"));
        assertTrue(result.contains("ISSUED"));
    }

    @Test
    void testPrintNullTicket() {
        TicketPrinter printer = new TicketPrinter();

        assertThrows(IllegalArgumentException.class,
                () -> printer.printTicket(null));
    }

    @Test
    void testSaveTicket(@TempDir Path tempDir)
            throws IOException {

        Ticket ticket = new Ticket(
                "T002", "R002", "B5", 12000.0
        );

        TicketPrinter printer = new TicketPrinter();
        printer.saveTicket(ticket, tempDir);

        Path file = tempDir.resolve("T002.txt");

        assertTrue(Files.exists(file));

        String content = Files.readString(file);

        assertTrue(content.contains("T002"));
        assertTrue(content.contains("B5"));
        assertTrue(content.contains("12000"));
    }

    @Test
    void testSaveNullTicket(@TempDir Path tempDir) {
        TicketPrinter printer = new TicketPrinter();

        assertThrows(IllegalArgumentException.class,
                () -> printer.saveTicket(null, tempDir));
    }

    @Test
    void testSaveNullDirectory() {
        Ticket ticket = new Ticket(
                "T003", "R003", "C5", 10000.0
        );

        TicketPrinter printer = new TicketPrinter();

        assertThrows(IllegalArgumentException.class,
                () -> printer.saveTicket(ticket, null));
    }
}
