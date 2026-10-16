
package com.concertticketing.facade;

import com.concertticketing.processing.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AdminFacadeTest {

    @TempDir
    Path tempDir;

    @Test
    void testNullDirectory() {
        assertThrows(IllegalArgumentException.class,
                () -> new AdminFacade(null));
    }

    @Test
    void testEmptyReservations() {
        AdminFacade admin = new AdminFacade(tempDir);

        assertEquals(0, admin.getTotalReservations());
        assertTrue(admin.getAllReservations().isEmpty());
    }

    @Test
    void testEmptyTickets() {
        AdminFacade admin = new AdminFacade(tempDir);

        assertEquals(0, admin.getTotalTickets());
        assertTrue(admin.getAllTickets().isEmpty());
    }

    @Test
    void testFindUnknownTicket() {
        AdminFacade admin = new AdminFacade(tempDir);

        assertNull(admin.findTicket("UNKNOWN"));
    }

    @Test
    void testFindUnknownReservation() {
        AdminFacade admin = new AdminFacade(tempDir);

        assertNull(admin.findReservation("UNKNOWN"));
    }

    @Test
    void testAdminSeesBookedTicket() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        Ticket ticket = facade.bookTicket("A1");

        AdminFacade admin = new AdminFacade(tempDir);

        assertEquals(1, admin.getTotalTickets());
        assertEquals(1, admin.getTotalReservations());
        assertNotNull(admin.findTicket(ticket.getTicketId()));
        assertNotNull(admin.findReservation(
                ticket.getReservationId()));
    }

    @Test
    void testAdminSeesDataAfterRestart() {
        ConcertTicketingFacade facade =
                new ConcertTicketingFacade(tempDir);

        facade.bookTicket("A1");

        AdminFacade restartedAdmin = new AdminFacade(tempDir);

        assertEquals(1, restartedAdmin.getTotalTickets());
        assertEquals(1, restartedAdmin.getTotalReservations());
    }
}
