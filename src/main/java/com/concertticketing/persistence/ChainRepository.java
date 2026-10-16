
package com.concertticketing.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ChainRepository {

    private final Path filePath;

    public ChainRepository(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path is required");
        }
        this.filePath = filePath;
    }

    public void save(ChainRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Chain record is required");
        }

        List<ChainRecord> records = findAll();

        records.removeIf(r ->
                r.getReservationId().equals(record.getReservationId()));

        records.add(record);

        List<String> lines = new ArrayList<>();

        for (ChainRecord r : records) {
            lines.add(r.getReservationId() + "," + r.getStatus().name());
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
            throw new IllegalStateException("Cannot save chain records", e);
        }
    }

    public List<ChainRecord> findAll() {
        List<ChainRecord> records = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return records;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length != 2) {
                    throw new IllegalStateException("Invalid chain data");
                }

                records.add(new ChainRecord(
                        parts[0],
                        ChainStatus.valueOf(parts[1])
                ));
            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot load chain records", e);
        }

        return records;
    }

    public ChainRecord findById(String reservationId) {
        for (ChainRecord record : findAll()) {
            if (record.getReservationId().equals(reservationId)) {
                return record;
            }
        }

        return null;
    }
}
