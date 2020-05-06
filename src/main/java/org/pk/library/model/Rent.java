package org.pk.library.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Rent {
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
        final StringBuilder sb = new StringBuilder("Rent{");
        sb.append("book=").append(BOOK);
        sb.append(", reader=").append(READER);
        sb.append(", dateOfRent=").append(dateOfRent);
        sb.append(", dateOfReturn=").append(dateOfReturn);
        sb.append(", returned=").append(returned);
        sb.append(", rentID='").append(RENT_ID).append('\'');
        sb.append('}');
        return sb.toString();
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
