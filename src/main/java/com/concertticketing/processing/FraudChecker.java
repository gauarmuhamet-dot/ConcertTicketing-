
package com.concertticketing.processing;

import java.util.HashSet;
import java.util.Set;

public class FraudChecker {

    private final Set<String> registeredTickets = new HashSet<>();

    public void registerTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket is required");
        }

        if (!registeredTickets.add(ticket.getTicketId())) {
            throw new IllegalStateException("Duplicate ticket ID");
        }
    }

    public boolean isValid(Ticket ticket) {
        if (ticket == null) {
            return false;
        }

        return registeredTickets.contains(ticket.getTicketId())
                && ticket.getStatus() == Ticket.Status.ISSUED;
    }

    public void validateTicket(Ticket ticket) {
        if (!isValid(ticket)) {
            throw new IllegalStateException("Invalid or already used ticket");
        }
    }

    public int getRegisteredTicketCount() {
        return registeredTickets.size();
    }
}
