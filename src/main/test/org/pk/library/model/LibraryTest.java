package org.pk.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    Library library;
    Book book1, book2, book3;
    Reader reader1, reader2;
    Rent rent1;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("5869782","Harry Potter", "J.K Rowling","Bloomsbury Publishing");
        book2 = new Book("5244322","Masło przygodowe", "Barbara Stenka", "Helion");
        book3 = new Book("8865433","Pulpet i Prudencja", "Joanna Olech", "PWN");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        reader1 = new Reader("Jan","Kowalski", LocalDate.of(1990,1,12),"666777888","jan.kowalski@gmail.com");
        reader2 = new Reader("Adrian","Kowalski",LocalDate.of(1994,4,21),"667677888","adrian.kowalski@gmail.com");
        library.addReader(reader1);
        library.addReader(reader2);
        rent1 = new Rent(book1,reader1, LocalDateTime.now(),LocalDateTime.of(2020,5,10,2,30),false);
        library.addRent(rent1);
    }

    @Test
    void getBooks() {
        assertEquals(3,library.getBooks().size());
    }

    @Test
    void getReaders() {
        assertEquals(2,library.getReaders().size());
    }

    @Test
    void getRents() {
        assertEquals(1,library.getRents().size());
    }

    @Test
    void addBook() {
        Book book4 = new Book("1277843","Cierpliwość fana fantastyki", "Piotr Siuda","Helion");
        library.addBook(book4);
        assertEquals(4,library.getNumberOfBooks());
    }

    @Test
    void getBook() {
        assertEquals(book1,library.getBook(0));
        assertEquals(null,library.getBook(7));
    }

    @Test
    void getNumberOfBooks() {
        assertEquals(3, library.getNumberOfBooks());
    }

    @Test
    void testToString() {
        assertEquals(" ",library.toString());
    }

    @Test
    void removeBook() {
        assertEquals(3, library.getNumberOfBooks());
        library.removeBook(book1);
        assertEquals(2,library.getNumberOfBooks());
    }
}