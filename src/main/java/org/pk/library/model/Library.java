package org.pk.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Library {
    private final List<Book> books;
    private final List<Reader> readers;
    private final List<Rent> rents;

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public Library() {
        books = new ArrayList<>();
        readers = new ArrayList<>();
        rents = new ArrayList<>();
    }

    public Library(List<Book> books, List<Reader> readers, List<Rent> rents) {
        this.books = books;
        this.readers = readers;
        this.rents = rents;
    }

    public void addBook(final Book book) {
        if (book.getIsbn() != null && book.getTitle() != null && book.getAuthor() != null && !books.contains(book))
            books.add(book);
    }

    public Book getBook(int index) {
        if(index >= books.size())
            return null;

        return books.get(index);
    }

    public int getNumberOfBooks() {
        return books.size();
    }

    @Override
    public String toString() {
        return "Library{" +
                "books=" + books +
                '}';
    }

    static class titleComparator implements Comparator<Book>{
        @Override
        public int compare(Book o1, Book o2) {
            int result = o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            return Integer.compare(result, 0);
        }
    }
    public void sortTitle() {
        books.sort(new titleComparator());
    }

    public void sortAuthor() {
        books.sort((o1, o2) -> {
            try {
                int result = o1.getAuthor().compareTo(o2.getAuthor());
                return Integer.compare(result, 0);
            } catch(NullPointerException e) {
                return 1;
            }
        });
    }
}
