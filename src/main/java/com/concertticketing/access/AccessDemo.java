
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;

import java.nio.file.Path;

public class AccessDemo {

    public static void main(String[] args) {

        TicketRepository repository =
                new TicketRepository(Path.of("data", "access_test_tickets.csv"));

        AccessControlGate gate = new AccessControlGate(repository);
        RefundService refundService = new RefundService(repository);

        // Test 1: Entry with a valid ticket
        Ticket ticket1 = new Ticket("TEST001", "R001", "A5", 15000);
        repository.save(ticket1);

        System.out.println("=== ACCESS CONTROL TEST ===");
        System.out.println("First entry: " + gate.allowEntry("TEST001"));
        System.out.println("Second entry: " + gate.allowEntry("TEST001"));

        // Test 2: Refund an unused ticket
        Ticket ticket2 = new Ticket("TEST002", "R002", "B10", 12000);
        repository.save(ticket2);

        System.out.println("\n=== REFUND TEST ===");
        System.out.println("Refund unused ticket: "
                + refundService.refundTicket("TEST002"));

        System.out.println("Entry after refund: "
                + gate.allowEntry("TEST002"));

        // Test 3: Refund an already used ticket
        System.out.println("\n=== USED TICKET TEST ===");
        System.out.println("Refund used ticket: "
                + refundService.refundTicket("TEST001"));

        // Test 4: Unknown ticket
        System.out.println("\n=== INVALID TICKET TEST ===");
        System.out.println("Unknown ticket entry: "
                + gate.allowEntry("UNKNOWN"));

        System.out.println("\n=== SAVED STATUSES ===");
        System.out.println("TEST001: "
                + repository.findById("TEST001").getStatus());
        System.out.println("TEST002: "
                + repository.findById("TEST002").getStatus());
    }
}
