package org.pk.library.model;

import java.util.Date;
import java.util.Objects;

public class Rent {
    private final Book book;
    private final Reader reader;
    private Date dateOfRent;
    private Date dateOfReturn;
    private boolean returned;

    public Rent(Book book, Reader reader, Date dateOfRent, Date dateOfReturn, boolean returned) {
        this.book = book;
        this.reader = reader;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
        this.returned = returned;
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public Date getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(Date dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
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
        sb.append('}');
        return sb.toString();
    }
}
