package org.pk.library.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalReminder {
    private final Rent rent;
    private LocalDateTime dateOfReminder;
    private final String REMINDER_ID;

    public RentalReminder(Rent rent, LocalDateTime dateOfReminder) {
        this.rent = rent;
        this.dateOfReminder = dateOfReminder;
        REMINDER_ID = UUID.randomUUID().toString();
    }

    public RentalReminder(Rent rent, LocalDateTime dateOfReminder, String REMINDER_ID) {
        this.rent = rent;
        this.dateOfReminder = dateOfReminder;
        this.REMINDER_ID = REMINDER_ID;
    }

    @Override
    public String toString() {
        return "RentalReminder{" + "rent=" + rent +
                ", dateOfReminder=" + dateOfReminder +
                '}';
    }

    public Rent getRent() {
        return rent;
    }

    public String getREMINDER_ID() {
        return REMINDER_ID;
    }

    public LocalDateTime getDateOfReminder() {
        return dateOfReminder;
    }

    public void setDateOfReminder(LocalDateTime dateOfReminder) {
        this.dateOfReminder = dateOfReminder;
    }
}
