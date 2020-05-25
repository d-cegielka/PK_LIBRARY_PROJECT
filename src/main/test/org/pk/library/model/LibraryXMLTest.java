package org.pk.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LibraryXMLTest {

    Library library;
    LibraryXML libraryXML;
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
    void saveDataToXML() throws IOException {
        libraryXML = new LibraryXML();
        try {
            libraryXML.saveDataToXML(library,"library.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readDataFromXML() {
    }
}