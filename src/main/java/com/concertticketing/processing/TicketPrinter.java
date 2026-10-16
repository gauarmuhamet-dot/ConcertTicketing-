
package com.concertticketing.processing;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class TicketPrinter {

    public String printTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket is required");
        }

        return """
                =========================
                    CONCERT TICKET
                =========================
                Ticket ID: %s
                Reservation ID: %s
                Seat: %s
                Price: %.2f KZT
                Status: %s
                =========================
                """.formatted(
                ticket.getTicketId(),
                ticket.getReservationId(),
                ticket.getSeatNumber(),
                ticket.getPrice(),
                ticket.getStatus()
        );
    }

    public void saveTicket(Ticket ticket, Path directory) {
        if (ticket == null || directory == null) {
            throw new IllegalArgumentException("Ticket and directory are required");
        }

        try {
            Files.createDirectories(directory);

            Path file = directory.resolve(ticket.getTicketId() + ".txt");

            Files.writeString(
                    file,
                    printTicket(ticket),
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new IllegalStateException("Cannot save ticket", e);
        }
    }
}
