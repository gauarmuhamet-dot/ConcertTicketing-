
package com.concertticketing.facade;

import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;
import com.concertticketing.reservation.ReservationRepository;
import com.concertticketing.reservation.SeatReservation;

import java.nio.file.Path;
import java.util.List;

public class AdminFacade {

    private final ReservationRepository reservations;
    private final TicketRepository tickets;

    public AdminFacade(Path dataDirectory) {
        if (dataDirectory == null) {
            throw new IllegalArgumentException("Data directory is required");
        }

        reservations = new ReservationRepository(
                dataDirectory.resolve("reservations.csv"));

        tickets = new TicketRepository(
                dataDirectory.resolve("tickets.csv"));
    }

    public List<SeatReservation> getAllReservations() {
        return reservations.findAll();
    }

    public List<Ticket> getAllTickets() {
        return tickets.findAll();
    }

    public SeatReservation findReservation(String reservationId) {
        return reservations.findById(reservationId);
    }

    public Ticket findTicket(String ticketId) {
        return tickets.findById(ticketId);
    }

    public int getTotalReservations() {
        return reservations.findAll().size();
    }

    public int getTotalTickets() {
        return tickets.findAll().size();
    }
}
