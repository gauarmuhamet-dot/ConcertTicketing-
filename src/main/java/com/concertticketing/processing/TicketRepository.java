
package com.concertticketing.processing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {

    private final Path filePath;

    public TicketRepository(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path is required");
        }
        this.filePath = filePath;
    }

    public void save(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket is required");
        }

        List<Ticket> tickets = findAll();

        tickets.removeIf(t ->
                t.getTicketId().equals(ticket.getTicketId()));

        tickets.add(ticket);

        List<String> lines = new ArrayList<>();

        for (Ticket t : tickets) {
            lines.add(
                    t.getTicketId() + "," +
                            t.getReservationId() + "," +
                            t.getSeatNumber() + "," +
                            t.getPrice() + "," +
                            t.getStatus().name()
            );
        }

        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    filePath,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new IllegalStateException("Cannot save tickets", e);
        }
    }

    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tickets;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length != 5) {
                    throw new IllegalStateException("Invalid ticket data");
                }

                Ticket ticket = new Ticket(
                        parts[0],
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3])
                );

                Ticket.Status status = Ticket.Status.valueOf(parts[4]);

                if (status == Ticket.Status.USED) {
                    ticket.markUsed();
                } else if (status == Ticket.Status.REFUNDED) {
                    ticket.refund();
                }

                tickets.add(ticket);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot load tickets", e);
        }

        return tickets;
    }

    public Ticket findById(String ticketId) {
        for (Ticket ticket : findAll()) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }

        return null;
    }
}
