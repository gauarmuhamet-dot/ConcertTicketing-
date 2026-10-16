
package com.concertticketing.access;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;

public class RefundService {

    private final TicketRepository repository;

    public RefundService(TicketRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required");
        }
        this.repository = repository;
    }

    public boolean refundTicket(String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            return false;
        }

        Ticket ticket = repository.findById(ticketId);

        if (ticket == null) {
            return false;
        }

        if (ticket.getStatus() != Ticket.Status.ISSUED) {
            return false;
        }

        ticket.refund();
        repository.save(ticket);

        return true;
    }
}
