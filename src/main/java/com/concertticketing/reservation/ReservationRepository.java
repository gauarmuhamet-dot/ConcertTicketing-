
package com.concertticketing.reservation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    private final Path filePath;

    public ReservationRepository(Path filePath) {
        this.filePath = filePath;
    }

    public void save(SeatReservation reservation) {
        List<SeatReservation> reservations = findAll();

        reservations.removeIf(r ->
                r.getReservationId().equals(reservation.getReservationId()));

        reservations.add(reservation);

        List<String> lines = new ArrayList<>();

        for (SeatReservation r : reservations) {
            lines.add(
                    r.getReservationId() + "," +
                            r.getSeatNumber() + "," +
                            r.getPrice() + "," +
                            r.getStatus().name()
            );
        }

        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    filePath,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new IllegalStateException("Cannot save reservations", e);
        }
    }

    public List<SeatReservation> findAll() {
        List<SeatReservation> reservations = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return reservations;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 4) {
                    throw new IllegalStateException("Invalid reservation data");
                }

                SeatReservation reservation = new SeatReservation(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2])
                );

                SeatReservation.Status status =
                        SeatReservation.Status.valueOf(parts[3]);

                if (status == SeatReservation.Status.RESERVED) {
                    reservation.reserve();
                } else if (status == SeatReservation.Status.CANCELLED) {
                    reservation.reserve();
                    reservation.cancel();
                }

                reservations.add(reservation);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot load reservations", e);
        }

        return reservations;
    }

    public SeatReservation findById(String reservationId) {
        for (SeatReservation reservation : findAll()) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }

        return null;
    }
}
