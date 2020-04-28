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

    public List<Reader> getReaders() {
        return Collections.unmodifiableList(readers);
    }

    public List<Rent> getRents() {
        return Collections.unmodifiableList(rents);
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
        books.add(book);
    }

    public void addReader(final Reader reader) {
        readers.add(reader);
    }

    public void addRent(final Rent rent) {
        rents.add(rent);
    }

    public boolean removeBook(final Book book){
        if(books.remove(book))
            return true;

        return false;
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
        final StringBuilder sb = new StringBuilder("Library{");
        sb.append("books=").append(books);
        sb.append(", readers=").append(readers);
        sb.append(", rents=").append(rents);
        sb.append('}');
        return sb.toString();
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
