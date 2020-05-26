package org.pk.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Library {
    /**
     * Lista książek.
     */
    private final List<Book> books;
    /**
     * Lista czytelników.
     */
    private final List<Reader> readers;
    /**
     * Lista wypożyczeń.
     */
    private final List<Rent> rents;
    /**
     * Lista przypomnień.
     */
    private final List<RentalReminder> rentalReminders;

    /**
     * Konstruktor bezparametrowy biblioteki.
     */
    public Library() {
        books = new ArrayList<>();
        readers = new ArrayList<>();
        rents = new ArrayList<>();
        rentalReminders = new ArrayList<>();
    }

    /**
     * Konstruktor parametrowy biblioteki.
     * @param books lista książek
     * @param readers lista czytelników
     * @param rents lista wypożyczeń
     * @param rentalReminders lista przypomnień
     */
    public Library(List<Book> books, List<Reader> readers, List<Rent> rents, List<RentalReminder> rentalReminders) {
        this.books = books;
        this.readers = readers;
        this.rents = rents;
        this.rentalReminders = rentalReminders;
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

    public boolean addRentalReminder(final RentalReminder rentalReminder) {
        return rentalReminders.add(rentalReminder);
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

    public boolean removeRentalReminder(final RentalReminder rentalReminder) {
        return rentalReminders.remove(rentalReminder);
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

    public List<RentalReminder> getRentalReminders() {
        return Collections.unmodifiableList(rentalReminders);
    }

    /**
     * Wyszukuje obiekt książki w liście książek po ID książki
     * @param books lista książek
     * @param book_id ID wyszukiwanej książki
     * @return obiekt typu Book z książką
     */
    public static Book findBookById(final List<Book> books, final String book_id)
    {
        return books.stream().filter(book -> book_id.equals(book.getBOOK_ID())).findAny().orElse(null);
    }

    /**
     * Wyszukuje obiekt czytelnika w liście czytelników po ID czytelnika
     * @param readers lista czytelników
     * @param reader_id ID wyszukiwanego czytelnika
     * @return obiekt typu Reader z czytelnikiem
     */
    public static Reader findReaderById(final List<Reader> readers, final String reader_id)
    {
        return readers.stream().filter(reader -> reader_id.equals(reader.getREADER_ID())).findAny().orElse(null);
    }

    /**
     * Wyszukuje obiekt wypożyczenia w liście wypożyczeń po ID wypożyczenia
     * @param rents lista wypożyczeń
     * @param rent_id ID wyszukiwanego wypożyczenia
     * @return obiekt typu Rent z wypożyczeniem
     */
    public static Rent findRentById(final List<Rent> rents, final String rent_id)
    {
        return rents.stream().filter(rent -> rent_id.equals(rent.getRENT_ID())).findAny().orElse(null);
    }

    @Override
    public String toString() {
        return "Library{" + "books=" + books +
                ", readers=" + readers +
                ", rents=" + rents +
                ", rentalReminders=" + rentalReminders +
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
