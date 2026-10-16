
package com.concertticketing.facade;

import com.concertticketing.persistence.ChainRecord;
import com.concertticketing.persistence.ChainRepository;
import com.concertticketing.persistence.ChainStatus;
import com.concertticketing.processing.Ticket;
import com.concertticketing.processing.TicketRepository;
import com.concertticketing.reservation.ReservationRepository;
import com.concertticketing.reservation.SeatReservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ConcertTicketingFacadeTest {

    @TempDir
    Path tempDir;

    @Test
    void testBookTicket() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket ticket = facade.bookTicket("A1");

        assertNotNull(ticket);
        assertEquals("A1", ticket.getSeatNumber());
        assertEquals(Ticket.Status.ISSUED, ticket.getStatus());
    }

    @Test
    void testDoubleBooking() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        facade.bookTicket("A2");

        assertThrows(IllegalStateException.class,
                () -> facade.bookTicket("A2"));
    }

    @Test
    void testEnterConcert() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket ticket = facade.bookTicket("A3");

        assertTrue(facade.enterConcert(ticket.getTicketId()));
        assertFalse(facade.enterConcert(ticket.getTicketId()));
    }

    @Test
    void testRefundTicket() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket ticket = facade.bookTicket("A4");

        assertTrue(facade.refundTicket(ticket.getTicketId()));
        assertFalse(facade.refundTicket(ticket.getTicketId()));
    }

    @Test
    void testRefundUsedTicket() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket ticket = facade.bookTicket("A5");

        facade.enterConcert(ticket.getTicketId());

        assertFalse(facade.refundTicket(ticket.getTicketId()));
    }

    @Test
    void testRebookAfterRefund() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket first = facade.bookTicket("A6");

        assertTrue(facade.refundTicket(first.getTicketId()));

        Ticket second = facade.bookTicket("A6");

        assertNotNull(second);
        assertNotEquals(
                first.getTicketId(),
                second.getTicketId()
        );
    }

    @Test
    void testPersistenceAfterRestart() {
        ConcertTicketingFacade firstFacade =
                new ConcertTicketingFacade(tempDir);

        firstFacade.bookTicket("A7");

        ConcertTicketingFacade secondFacade =
                new ConcertTicketingFacade(tempDir);

        assertThrows(IllegalStateException.class,
                () -> secondFacade.bookTicket("A7"));
    }

    @Test
    void testUnknownTicket() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        assertFalse(facade.enterConcert("UNKNOWN"));
        assertFalse(facade.refundTicket("UNKNOWN"));
    }

    @Test
    void testNullDirectory() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConcertTicketingFacade(null));
    }

    // B FAILURE: Undo reservation A
    @Test
    void testBFailureCompensatesReservation() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        int seatsBefore = facade.getAvailableSeats();

        assertThrows(IllegalStateException.class,
                () -> facade.bookTicketWithFailure("B1", "B"));

        ReservationRepository reservations =
                new ReservationRepository(
                        tempDir.resolve("reservations.csv"));

        SeatReservation reservation =
                reservations.findAll().get(0);

        assertEquals(
                SeatReservation.Status.CANCELLED,
                reservation.getStatus()
        );

        assertEquals(
                seatsBefore,
                facade.getAvailableSeats()
        );

        assertEquals(
                ChainStatus.COMPENSATED,
                facade.getChains().get(0).getStatus()
        );
    }

    // C FAILURE: Undo ticket B, then reservation A
    @Test
    void testCFailureCompensatesTicketAndReservation() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        int seatsBefore = facade.getAvailableSeats();

        assertThrows(IllegalStateException.class,
                () -> facade.bookTicketWithFailure("B2", "C"));

        ReservationRepository reservations =
                new ReservationRepository(
                        tempDir.resolve("reservations.csv"));

        TicketRepository tickets =
                new TicketRepository(
                        tempDir.resolve("tickets.csv"));

        SeatReservation reservation =
                reservations.findAll().get(0);

        Ticket ticket = tickets.findAll().get(0);

        assertEquals(
                Ticket.Status.REFUNDED,
                ticket.getStatus()
        );

        assertEquals(
                SeatReservation.Status.CANCELLED,
                reservation.getStatus()
        );

        assertEquals(
                seatsBefore,
                facade.getAvailableSeats()
        );

        assertEquals(
                ChainStatus.COMPENSATED,
                facade.getChains().get(0).getStatus()
        );
    }

    // RECOVERY: Restart while chain is PENDING_B
    @Test
    void testRecoveryFromPendingB() {
        ReservationRepository reservations =
                new ReservationRepository(
                        tempDir.resolve("reservations.csv"));

        ChainRepository chains =
                new ChainRepository(
                        tempDir.resolve("chains.csv"));

        SeatReservation reservation =
                new SeatReservation(
                        "recovery-b", "C1", 10000.0);

        reservation.reserve();
        reservations.save(reservation);

        chains.save(new ChainRecord(
                "recovery-b", ChainStatus.PENDING_B));

        // Simulate application restart
        ConcertTicketingFacade restarted =
                new ConcertTicketingFacade(tempDir);

        assertEquals(
                SeatReservation.Status.CANCELLED,
                reservations.findById("recovery-b").getStatus()
        );

        assertEquals(
                ChainStatus.COMPENSATED,
                chains.findById("recovery-b").getStatus()
        );

        assertEquals(60, restarted.getAvailableSeats());
    }

    // RECOVERY: Restart while chain is PENDING_C
    @Test
    void testRecoveryFromPendingC() {
        ReservationRepository reservations =
                new ReservationRepository(
                        tempDir.resolve("reservations.csv"));

        TicketRepository tickets =
                new TicketRepository(
                        tempDir.resolve("tickets.csv"));

        ChainRepository chains =
                new ChainRepository(
                        tempDir.resolve("chains.csv"));

        SeatReservation reservation =
                new SeatReservation(
                        "recovery-c", "C2", 10000.0);

        reservation.reserve();
        reservations.save(reservation);

        Ticket ticket =
                new Ticket(
                        "recovery-ticket",
                        "recovery-c",
                        "C2",
                        10000.0);

        tickets.save(ticket);

        chains.save(new ChainRecord(
                "recovery-c", ChainStatus.PENDING_C));

        // Simulate application restart
        ConcertTicketingFacade restarted =
                new ConcertTicketingFacade(tempDir);

        assertEquals(
                Ticket.Status.REFUNDED,
                tickets.findById("recovery-ticket").getStatus()
        );

        assertEquals(
                SeatReservation.Status.CANCELLED,
                reservations.findById("recovery-c").getStatus()
        );

        assertEquals(
                ChainStatus.COMPENSATED,
                chains.findById("recovery-c").getStatus()
        );

        assertEquals(60, restarted.getAvailableSeats());
    }
}
