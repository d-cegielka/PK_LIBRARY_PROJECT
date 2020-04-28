package org.pk.library.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Rent {
    private final Book book;
    private final Reader reader;
    private LocalDate dateOfRent;
    private LocalDate dateOfReturn;
    private boolean returned;
    private final String rentID;

    public Rent(Book book, Reader reader, LocalDate dateOfRent, LocalDate dateOfReturn, boolean returned) {
        this.rentID = UUID.randomUUID().toString();
        this.book = book;
        this.reader = reader;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
        this.returned = returned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent)) return false;
        Rent rent = (Rent) o;
        return Objects.equals(book, rent.book) &&
                Objects.equals(reader, rent.reader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, reader);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rent{");
        sb.append("book=").append(book);
        sb.append(", reader=").append(reader);
        sb.append(", dateOfRent=").append(dateOfRent);
        sb.append(", dateOfReturn=").append(dateOfReturn);
        sb.append(", returned=").append(returned);
        sb.append(", rentID='").append(rentID).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
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

    public String getRentID() {
        return rentID;
    }
}
