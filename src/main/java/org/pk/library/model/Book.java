package org.pk.library.model;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Comparable<Book>, Serializable {
    private String isbn;
    private String title;
    private String author;
    private String publisher;

    public Book(String isbn, String title, String author, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "Book{" +
                "ISBN: " + isbn +
                ", Title: '" + title + '\'' +
                ", Author: '" + author + '\'' +
                ", Publisher: '" + author + '\'' +
                '}';
    }

    @Override
    public int compareTo(Book o) {
        int result = this.title.compareTo(o.title);
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

}
