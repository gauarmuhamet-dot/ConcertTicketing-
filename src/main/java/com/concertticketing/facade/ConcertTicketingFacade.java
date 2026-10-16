
package com.concertticketing.facade;

import com.concertticketing.access.AccessControlGate;
import com.concertticketing.access.RefundService;
import com.concertticketing.persistence.ChainRecord;
import com.concertticketing.persistence.ChainRepository;
import com.concertticketing.persistence.ChainStatus;
import com.concertticketing.processing.*;
import com.concertticketing.reservation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class ConcertTicketingFacade {

    private final SeatingChart chart = new SeatingChart();
    private final DynamicPricingEngine pricing = new DynamicPricingEngine();

    private final ReservationRepository reservations;
    private final TicketRepository tickets;
    private final ChainRepository chains;
    private final AccessControlGate gate;
    private final RefundService refunds;
    private final TicketPrinter printer = new TicketPrinter();

    public ConcertTicketingFacade(Path dataDirectory) {
        if (dataDirectory == null) {
            throw new IllegalArgumentException(
                    "Data directory is required");
        }

        reservations = new ReservationRepository(
                dataDirectory.resolve("reservations.csv"));

        tickets = new TicketRepository(
                dataDirectory.resolve("tickets.csv"));

        chains = new ChainRepository(
                dataDirectory.resolve("chains.csv"));

        gate = new AccessControlGate(tickets);
        refunds = new RefundService(tickets);

        // Restore occupied seats after restart
        for (SeatReservation reservation : reservations.findAll()) {
            if (reservation.getStatus()
                    == SeatReservation.Status.RESERVED) {
                chart.reserveSeat(reservation.getSeatNumber());
            }
        }

        // Recover unfinished operations after restart
        recoverUnfinishedChains();
    }

    public Ticket bookTicket(String seatNumber) {
        return bookTicketWithFailure(seatNumber, "NONE");
    }

    // Failure modes: NONE, B, C
    public Ticket bookTicketWithFailure(
            String seatNumber, String failureMode) {

        if (!chart.isAvailable(seatNumber)) {
            throw new IllegalStateException(
                    "Seat is not available");
        }

        if (!"NONE".equals(failureMode)
                && !"B".equals(failureMode)
                && !"C".equals(failureMode)) {
            throw new IllegalArgumentException(
                    "Invalid failure mode");
        }

        double price = pricing.calculatePrice(seatNumber);

        String reservationId = UUID.randomUUID().toString();
        String ticketId = UUID.randomUUID().toString();

        // STEP A: Reservation
        SeatReservation reservation =
                new SeatReservation(
                        reservationId, seatNumber, price);

        reservation.reserve();
        reservations.save(reservation);
        chart.reserveSeat(seatNumber);

        ChainRecord chain = new ChainRecord(
                reservationId, ChainStatus.PENDING_B);

        chains.save(chain);

        try {
            // STEP B: Ticket processing
            if ("B".equals(failureMode)) {
                throw new IllegalStateException(
                        "Simulated failure at step B");
            }

            Ticket ticket = new Ticket(
                    ticketId, reservationId,
                    seatNumber, price);

            tickets.save(ticket);

            chain.setStatus(ChainStatus.PENDING_C);
            chains.save(chain);


            // STEP C: Access Control
            if ("C".equals(failureMode)) {
                throw new IllegalStateException(
                        "Simulated failure at step C");
            }

            // Validate ticket without marking it as USED
            if (!gate.validateTicket(ticketId)) {
                throw new IllegalStateException(
                        "Ticket validation failed");
            }

            // Print ticket after successful validation
            printer.saveTicket(
                    ticket,
                    Path.of("data", "printed_tickets"));

            chain.setStatus(ChainStatus.COMPLETED);
            chains.save(chain);

            return ticket;

        } catch (RuntimeException e) {
            // Read persisted progress before compensation
            ChainRecord savedChain =
                    chains.findById(reservationId);

            if (savedChain != null) {
                // Undo B if a ticket was saved
                compensateTicket(reservationId);

                // Undo A
                compensateReservation(reservationId);

                savedChain.setStatus(
                        ChainStatus.COMPENSATED);

                chains.save(savedChain);
            }

            throw new IllegalStateException(
                    "Booking failed and was compensated: "
                            + e.getMessage(), e);
        }
    }

    // Recover PENDING_B and PENDING_C after restart
    private void recoverUnfinishedChains() {
        for (ChainRecord chain : chains.findAll()) {
            ChainStatus status = chain.getStatus();

            if (status == ChainStatus.PENDING_B
                    || status == ChainStatus.PENDING_C) {

                String reservationId =
                        chain.getReservationId();

                // Undo B first if ticket exists
                compensateTicket(reservationId);

                // Undo A second
                compensateReservation(reservationId);

                // Persist recovery result
                chain.setStatus(
                        ChainStatus.COMPENSATED);

                chains.save(chain);
            }
        }
    }

    private void compensateTicket(String reservationId) {
        for (Ticket ticket : tickets.findAll()) {
            if (ticket.getReservationId()
                    .equals(reservationId)
                    && ticket.getStatus()
                    == Ticket.Status.ISSUED) {

                ticket.refund();
                tickets.save(ticket);
            }
        }
    }

    private void compensateReservation(String reservationId) {
        SeatReservation reservation =
                reservations.findById(reservationId);

        if (reservation != null
                && reservation.getStatus()
                == SeatReservation.Status.RESERVED) {

            reservation.cancel();
            reservations.save(reservation);

            chart.releaseSeat(
                    reservation.getSeatNumber());
        }
    }

    public List<ChainRecord> getChains() {
        return chains.findAll();
    }

    public boolean enterConcert(String ticketId) {
        return gate.allowEntry(ticketId);
    }

    public boolean refundTicket(String ticketId) {
        Ticket ticket = tickets.findById(ticketId);

        if (ticket == null
                || ticket.getStatus()
                != Ticket.Status.ISSUED) {
            return false;
        }

        if (!refunds.refundTicket(ticketId)) {
            return false;
        }

        SeatReservation reservation =
                reservations.findById(
                        ticket.getReservationId());

        if (reservation != null
                && reservation.getStatus()
                == SeatReservation.Status.RESERVED) {

            reservation.cancel();
            reservations.save(reservation);

            chart.releaseSeat(
                    reservation.getSeatNumber());
        }

        return true;
    }

    public int getAvailableSeats() {
        return chart.getAvailableSeatCount();
    }
}
