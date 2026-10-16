
package com.concertticketing.app;

import com.concertticketing.facade.ConcertTicketingFacade;
import com.concertticketing.persistence.ChainRecord;
import com.concertticketing.processing.Ticket;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ConcertTicketingFacade system =
                new ConcertTicketingFacade(
                        Path.of("data", "concert_demo"));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CONCERT TICKETING SYSTEM ===");
            System.out.println("1. Book ticket");
            System.out.println("2. Enter concert");
            System.out.println("3. Refund ticket");
            System.out.println("4. Show available seats");
            System.out.println("5. Simulate B failure");
            System.out.println("6. Simulate C failure");
            System.out.println("7. Show chain statuses");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Enter seat number: ");
                        String seat = scanner.nextLine().trim();

                        Ticket ticket = system.bookTicket(seat);

                        System.out.println("Booking successful!");
                        System.out.println("Ticket ID: " + ticket.getTicketId());
                        System.out.println("Seat: " + ticket.getSeatNumber());
                        System.out.println("Price: " + ticket.getPrice());
                    }

                    case "2" -> {
                        System.out.print("Enter ticket ID: ");
                        String id = scanner.nextLine().trim();

                        System.out.println("Entry allowed: "
                                + system.enterConcert(id));
                    }

                    case "3" -> {
                        System.out.print("Enter ticket ID: ");
                        String id = scanner.nextLine().trim();

                        System.out.println("Refund successful: "
                                + system.refundTicket(id));
                    }

                    case "4" -> System.out.println(
                            "Available seats: "
                                    + system.getAvailableSeats());

                    case "5", "6" -> {
                        System.out.print("Enter seat number: ");
                        String seat = scanner.nextLine().trim();

                        String failureMode =
                                choice.equals("5") ? "B" : "C";

                        system.bookTicketWithFailure(seat, failureMode);
                    }

                    case "7" -> {
                        System.out.println("\n=== SAVED CHAINS ===");

                        for (ChainRecord chain : system.getChains()) {
                            System.out.println(
                                    chain.getReservationId()
                                            + " -> "
                                            + chain.getStatus());
                        }
                    }

                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
}
