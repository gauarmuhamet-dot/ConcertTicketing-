
package com.concertticketing.reservation;

import java.nio.file.Path;

public class ReservationDemo {

    public static void main(String[] args) {

        DynamicPricingEngine pricing = new DynamicPricingEngine();
        SeatingChart chart = new SeatingChart();

        ReservationRepository repository =
                new ReservationRepository(Path.of("data", "reservations.csv"));

        String seat = "A5";
        double price = pricing.calculatePrice(seat);

        SeatReservation existing = repository.findById("R001");

        if (existing == null) {
            chart.reserveSeat(seat);

            SeatReservation reservation =
                    new SeatReservation("R001", seat, price);

            reservation.reserve();
            repository.save(reservation);
        }

        SeatReservation saved = repository.findById("R001");

        System.out.println("Reservation ID: " + saved.getReservationId());
        System.out.println("Seat: " + saved.getSeatNumber());
        System.out.println("Price: " + saved.getPrice());
        System.out.println("Status: " + saved.getStatus());
    }
}
