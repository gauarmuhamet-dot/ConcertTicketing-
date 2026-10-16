
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;

public class AccessControlGate {

    private final TicketRepository repository;

    public AccessControlGate(TicketRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required");
        }
        this.repository = repository;
    }

    // Check ticket without using it
    public boolean validateTicket(String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            return false;
        }

        Ticket ticket = repository.findById(ticketId);

        return ticket != null
                && ticket.getStatus() == Ticket.Status.ISSUED;
    }

    // Allow actual entry to the concert
    public boolean allowEntry(String ticketId) {
        if (!validateTicket(ticketId)) {
            return false;
        }

        Ticket ticket = repository.findById(ticketId);

        ticket.markUsed();
        repository.save(ticket);

        return true;
    }
}
