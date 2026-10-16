
package com.concertticketing.processing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FraudCheckerTest {

    @Test
    void testRegisterTicket() {
        FraudChecker checker = new FraudChecker();
        Ticket ticket = new Ticket(
                "T001", "R001", "A5", 15000.0
        );

        checker.registerTicket(ticket);

        assertEquals(1, checker.getRegisteredTicketCount());
        assertTrue(checker.isValid(ticket));
    }

    @Test
    void testDuplicateTicket() {
        FraudChecker checker = new FraudChecker();
        Ticket ticket = new Ticket(
                "T002", "R002", "B5", 12000.0
        );

        checker.registerTicket(ticket);

        assertThrows(IllegalStateException.class,
                () -> checker.registerTicket(ticket));
    }

    @Test
    void testUsedTicket() {
        FraudChecker checker = new FraudChecker();
        Ticket ticket = new Ticket(
                "T003", "R003", "C5", 10000.0
        );

        checker.registerTicket(ticket);
        ticket.markUsed();

        assertFalse(checker.isValid(ticket));
    }

    @Test
    void testUnregisteredTicket() {
        FraudChecker checker = new FraudChecker();
        Ticket ticket = new Ticket(
                "T004", "R004", "A6", 15000.0
        );

        assertFalse(checker.isValid(ticket));
    }

    @Test
    void testNullTicket() {
        FraudChecker checker = new FraudChecker();

        assertThrows(IllegalArgumentException.class,
                () -> checker.registerTicket(null));

        assertFalse(checker.isValid(null));
    }

    @Test
    void testValidateTicket() {
        FraudChecker checker = new FraudChecker();
        Ticket ticket = new Ticket(
                "T005", "R005", "B6", 12000.0
        );

        checker.registerTicket(ticket);

        assertDoesNotThrow(
                () -> checker.validateTicket(ticket));

        ticket.markUsed();

        assertThrows(IllegalStateException.class,
                () -> checker.validateTicket(ticket));
    }
}
