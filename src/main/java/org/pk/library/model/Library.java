package org.pk.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Library {
    private final List<Book> books;
    private final List<Reader> readers;
    private final List<Rent> rents;

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

    public boolean addBook(final Book book) {
        return books.add(book);
    }

    public boolean addReader(final Reader reader) {
        return readers.add(reader);
    }

    public boolean addRent(final Rent rent) {
        return rents.add(rent);
    }

    public boolean removeBook(final Book book){
        return books.remove(book);
    }

    public boolean removeReader(final Reader reader){
        return readers.remove(reader);
    }

    public void removeRent(final Rent rent){
        rents.remove(rent);
    }

    public Book getBook(int index) {
        if(index >= books.size())
            return null;

        return books.get(index);
    }

    public int getNumberOfBooks() {
        return books.size();
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public List<Reader> getReaders() {
        return Collections.unmodifiableList(readers);
    }

    public List<Rent> getRents() {
        return Collections.unmodifiableList(rents);
    }

    @Override
    public String toString() {
        return "Library{" + "books=" + books +
                ", readers=" + readers +
                ", rents=" + rents +
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
