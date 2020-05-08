package org.pk.library.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Rent extends RecursiveTreeObject<Rent> {
    private final Book BOOK;
    private final Reader READER;
    private LocalDate dateOfRent;
    private LocalDate dateOfReturn;
    private boolean returned;
    private final String RENT_ID;

    public Rent(Book BOOK, Reader READER, LocalDate dateOfRent, LocalDate dateOfReturn, boolean returned) {
        this.RENT_ID = UUID.randomUUID().toString();
        this.BOOK = BOOK;
        this.READER = READER;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
        this.returned = returned;
    }

    public Rent(Book BOOK, Reader READER, LocalDate dateOfRent, LocalDate dateOfReturn, boolean returned, String RENT_ID) {
        this.BOOK = BOOK;
        this.READER = READER;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
        this.returned = returned;
        this.RENT_ID = RENT_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent)) return false;
        Rent rent = (Rent) o;
        return Objects.equals(BOOK, rent.BOOK) &&
                Objects.equals(READER, rent.READER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BOOK, READER);
    }


    @Override
    public String toString() {
        return "Rent{" + "book=" + BOOK +
                ", reader=" + READER +
                ", dateOfRent=" + dateOfRent +
                ", dateOfReturn=" + dateOfReturn +
                ", returned=" + returned +
                ", rentID='" + RENT_ID + '\'' +
                '}';
    }

    public Book getBOOK() {
        return BOOK;
    }

    public Reader getREADER() {
        return READER;
    }

    public LocalDate getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(LocalDate dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public LocalDate getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDate dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public String getRENT_ID() {
        return RENT_ID;
    }
}
