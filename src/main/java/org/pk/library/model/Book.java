package org.pk.library.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Book extends RecursiveTreeObject<Book> implements Comparable<Book>, Serializable {
    private StringProperty isbn;
    private StringProperty title;
    private StringProperty author;
    private StringProperty publisher;
    private final StringProperty bookID;

    public Book(String isbn, String title, String author, String publisher) {
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.bookID = new SimpleStringProperty(UUID.randomUUID().toString());
    }

    public Book(String isbn, String title, String author, String publisher, String bookID) {
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.bookID = new SimpleStringProperty(bookID);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("isbn='").append(isbn).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", bookID='").append(bookID).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Book o) {
        int result = this.title.toString().compareTo(o.title.toString());
        return Integer.compare(result, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(publisher, book.publisher);

    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author, publisher);
    }


   /* public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBookID() {
        return bookID;
    }*/

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getBookID() {
        return bookID.get();
    }

    public StringProperty bookIDProperty() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID.set(bookID);
    }
}
