package org.pk.library.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Klasa reprezentująca książkę.
 */
public class Book extends RecursiveTreeObject<Book> {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private final String BOOK_ID;

    public Book(String isbn, String title, String author, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.BOOK_ID = UUID.randomUUID().toString();
    }

    public Book(String isbn, String title, String author, String publisher, String BOOK_ID) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.BOOK_ID = BOOK_ID;
    }

    @Override
    public String toString() {
        return "Book{" + "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", bookID='" + BOOK_ID + '\'' +
                '}';
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

    public String getIsbn() {
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

    public String getBOOK_ID() {
        return BOOK_ID;
    }
}
