package org.pk.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LibraryXMLTest {

    Library library;
    LibraryXML libraryXML;
    Book book1, book2, book3;
    Reader reader1, reader2;
    Rent rent1;
    RentalReminder rentalReminder1;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("5869782","Harry Potter", "J.K Rowling","Bloomsbury Publishing","0f789467-92b9-4c58-80f0-856338ad8230");
        book2 = new Book("5244322","Masło przygodowe", "Barbara Stenka", "Helion","0f571dd0-3d4c-4d93-9e03-2285c7036642");
        book3 = new Book("8865433","Pulpet i Prudencja", "Joanna Olech", "PWN", "9086bd17-bb6e-4fcc-8f87-d0528d61c13f");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        reader1 = new Reader("Jan","Kowalski", LocalDate.of(1990,1,12),"666777888","jan.kowalski@gmail.com", "7da9d877-3530-46e8-a2b9-f4731fff5726");
        reader2 = new Reader("Adrian","Kowalski",LocalDate.of(1994,4,21),"667677888","adrian.kowalski@gmail.com", "5101a69a-d32f-4ced-acb3-6b44c959bd99");
        library.addReader(reader1);
        library.addReader(reader2);
        rent1 = new Rent(book1,reader1, LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0,0)),
                LocalDateTime.of(2020,5,10,2,30,1,1),false, "7da9d877-3530-46e8-a2b9-f4731fff5726");
        library.addRent(rent1);
        rentalReminder1 = new RentalReminder(rent1,LocalDateTime.of(2020,5,8,2,30,1,1),"75daf846-c56b-4c8b-8073-074ee34d9004");
        library.addRentalReminder(rentalReminder1);
    }

    @Test
    void saveDataToXML() {
        libraryXML = new LibraryXML();
        try {
            libraryXML.saveDataToXML(library, "library.xml");
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readDataFromXML() {
        libraryXML = new LibraryXML();
        Library readLibrary = null;
        try {
           readLibrary = libraryXML.readDataFromXML("library.xml");
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(library.toString() ,readLibrary.toString());
    }
}