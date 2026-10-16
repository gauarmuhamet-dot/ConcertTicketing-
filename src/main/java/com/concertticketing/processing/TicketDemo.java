
package com.concertticketing.processing;

import java.nio.file.Path;

public class TicketDemo {

    public static void main(String[] args) {

        TicketRepository repository =
                new TicketRepository(Path.of("data", "tickets.csv"));

        FraudChecker fraudChecker = new FraudChecker();
        TicketPrinter printer = new TicketPrinter();

        Ticket ticket = repository.findById("T001");

        if (ticket == null) {
            ticket = new Ticket("T001", "R001", "A5", 15000.0);
            repository.save(ticket);
        }

        fraudChecker.registerTicket(ticket);

        System.out.println("=== TICKET INFORMATION ===");
        System.out.println(printer.printTicket(ticket));

        System.out.println("Fraud check: "
                + fraudChecker.isValid(ticket));

        printer.saveTicket(ticket, Path.of("data", "printed_tickets"));

        System.out.println("Ticket saved successfully!");
    }
}
