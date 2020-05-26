package org.pk.library.model;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibraryXML {
    List<Book> books;
    List<Reader> readers;
    List<Rent> rents;
    List<RentalReminder> rentalReminders;

    /**
     * Zapis biblioteki do pliku XML.
     * @param library obiekt klasy Library
     * @param filePath ścieżka do pliku
     * @throws IOException wyjątek IO
     * @throws XMLStreamException wyjątek XMLStream
     */
    public void saveDataToXML(Library library, String filePath) throws IOException, XMLStreamException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(os,"utf-8");
        writeLibraryToXML(writer, library);
        writer.close();
    }

    /**
     * Metoda zapisująca obiekty biblioteki do pliku XML.
     * @param writer stream XML
     * @param library obiekt biblioteki do zapisu
     * @throws XMLStreamException wyjątek XMLStream
     */
    private void writeLibraryToXML(XMLStreamWriter writer, Library library) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement("library");
        writer.writeCharacters("\n\t");

        writer.writeStartElement("books");
        writer.writeCharacters("\n");
        for (Book book : library.getBooks())
            writeBookElement(writer, book);
        writer.writeEndElement();
        writer.writeCharacters("\n\t");

        writer.writeStartElement("readers");
        writer.writeCharacters("\n");
        for (Reader reader: library.getReaders())
            writeReaderElement(writer,reader);
        writer.writeEndElement();
        writer.writeCharacters("\n\t");

        writer.writeStartElement("rents");
        writer.writeCharacters("\n");
        for(Rent rent: library.getRents())
            writeRentElement(writer,rent);
        writer.writeEndElement();
        writer.writeCharacters("\n\t");

        writer.writeStartElement("rentalReminders");
        writer.writeCharacters("\n");
        for(RentalReminder rentalReminder: library.getRentalReminders())
            writeRentalReminderElement(writer,rentalReminder);
        writer.writeEndElement();
        writer.writeCharacters("\n");

        writer.writeEndDocument();
    }

    /**
     * Metoda zapisująca w pliku XML informacje dotyczące książki.
     * @param writer stream XML
     * @param book obiekt klasy Book
     * @throws XMLStreamException wyjątek XMLStream
     */
    private void writeBookElement(XMLStreamWriter writer, Book book) throws XMLStreamException {
        writer.writeCharacters("\t\t");
        writer.writeStartElement("book");
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("isbn");
            writer.writeCharacters(book.getIsbn());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("title");
            writer.writeCharacters(book.getTitle());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("author");
            writer.writeCharacters(book.getAuthor());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("publisher");
            writer.writeCharacters(book.getPublisher());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("book_id");
            writer.writeCharacters(book.getBOOK_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t");
        writer.writeEndElement();
        writer.writeCharacters("\n\t");

    }

    /**
     * Metoda zapisująca w pliku XML informacje dotyczące czytelnika.
     * @param writer stream XML
     * @param reader obiekt klasy Reader
     * @throws XMLStreamException wyjątek XMLStream
     */
    private void writeReaderElement(XMLStreamWriter writer, Reader reader) throws XMLStreamException {
        writer.writeCharacters("\t\t");
        writer.writeStartElement("reader");
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("first_name");
            writer.writeCharacters(reader.getFirstName());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("last_name");
            writer.writeCharacters(reader.getLastName());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("date_of_birth");
            writer.writeCharacters(reader.getDateOfBirth().toString());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("phone_number");
            writer.writeCharacters(reader.getPhoneNumber());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("email");
            writer.writeCharacters(reader.getEmailAddress());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("reader_id");
            writer.writeCharacters(reader.getREADER_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t");
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
    }

    /**
     * Metoda zapisująca w pliku XML informacje dotyczące wypożyczenia.
     * @param writer stream XML
     * @param rent obiekt klasy Rent
     * @throws XMLStreamException wyjątek XMLStream
     */
    private void writeRentElement(XMLStreamWriter writer, Rent rent) throws XMLStreamException {
        writer.writeCharacters("\t\t");
        writer.writeStartElement("rent");
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("book_id");
            writer.writeCharacters(rent.getBOOK().getBOOK_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("reader_id");
            writer.writeCharacters(rent.getREADER().getREADER_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("date_of_rent");
            writer.writeCharacters(rent.getDateOfRent().toString());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("date_of_return");
            writer.writeCharacters(rent.getDateOfReturn().toString());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("returned");
            writer.writeCharacters(String.valueOf(rent.isReturned()));
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("rent_id");
            writer.writeCharacters(rent.getRENT_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t");
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
    }

    /**
     * Metoda zapisująca w pliku XML informacje dotyczące przypomnień.
     * @param writer stream XML
     * @param rentalReminder obiekt klasy RentaReminder
     * @throws XMLStreamException wyjątek XMLStream
     */
    private void writeRentalReminderElement(XMLStreamWriter writer, RentalReminder rentalReminder) throws XMLStreamException {
        writer.writeCharacters("\t\t");
        writer.writeStartElement("rentalReminder");
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("rent_id");
        writer.writeCharacters(rentalReminder.getRent().getRENT_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("date_of_reminder");
        writer.writeCharacters(rentalReminder.getDateOfReminder().toString());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t\t");

        writer.writeStartElement("rentalreminder_id");
        writer.writeCharacters(rentalReminder.getREMINDER_ID());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t");
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
    }

    /**
     * Metoda odczytujące obiekt klasy Library z pliku XML.
     * @param filePath ścieżka do pliku XML
     * @return obiekt klasy Library
     * @throws XMLStreamException wyjątek XMLStream
     * @throws FileNotFoundException wyjątek FNF
     */
    public Library readDataFromXML(String filePath) throws XMLStreamException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(filePath));
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader library = null;
        try {
            library = inputFactory.createXMLStreamReader(inputStream);
            return readDocument(library);
        } finally {
            if(library != null) {
                library.close();
            }
        }
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML.
     * @param library XMLStream
     * @return obiekt klasy Library
     * @throws XMLStreamException wyjątek XMLStream
     */
    private Library readDocument(XMLStreamReader library) throws XMLStreamException {
        while (library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("library"))
                        return readLibrary(library);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML i tworząca biblioteke.
     * @param library XMLStream
     * @return obiekt klasy Library
     * @throws XMLStreamException wyjątek XMLStream
     */
    private Library readLibrary(XMLStreamReader library) throws XMLStreamException {
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("books"))
                        books = readBooks(library);
                    if(elementName.equals("readers"))
                        readers = readReaders(library);
                    if(elementName.equals("rents"))
                        rents = readRents(library);
                    if(elementName.equals("rentalReminders"))
                        rentalReminders = readRentalReminders(library);
                   break;
                case XMLStreamReader.END_ELEMENT:
                    return new Library(books,readers,rents,rentalReminders);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. książek.
     * @param library XMLStream
     * @return lista książek
     * @throws XMLStreamException wyjątek XMLStream
     */
    private List<Book> readBooks(XMLStreamReader library) throws XMLStreamException {
        List<Book> books = new ArrayList<>();
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType){
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("book"))
                        books.add(readBook(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return books;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. książki.
     * @param library XMLStream
     * @return obiekt klasy Book
     * @throws XMLStreamException wyjątek XMLStream
     */
    private Book readBook(XMLStreamReader library) throws XMLStreamException {
        String[] book = new String[5];
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    switch (elementName) {
                        case "isbn":
                            book[0] = (readCharacters(library));
                            break;
                        case "title":
                            book[1] = (readCharacters(library));
                            break;
                        case "author":
                            book[2] = (readCharacters(library));
                            break;
                        case "publisher":
                            book[3] = (readCharacters(library));
                            break;
                        case "book_id":
                            book[4] = (readCharacters(library));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return new Book(book[0],book[1],book[2],book[3],book[4]);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. czytelników.
     * @param library XMLStream
     * @return lista czytelników
     * @throws XMLStreamException wyjątek XMLStream
     */
    private List<Reader> readReaders(XMLStreamReader library) throws XMLStreamException {
        List<Reader> readers = new ArrayList<>();
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType){
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("reader"))
                        readers.add(readReader(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return readers;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. czytelnika.
     * @param library XMLStream
     * @return obiekt klasy Reader
     * @throws XMLStreamException wyjątek XMLStream
     */
   private Reader readReader(XMLStreamReader library) throws XMLStreamException {
       String[] reader = new String[6];
       while(library.hasNext()) {
           int eventType = library.next();
           switch (eventType) {
               case XMLStreamReader.START_ELEMENT:
                   String elementName = library.getLocalName();
                   switch (elementName) {
                       case "first_name":
                           reader[0] = (readCharacters(library));
                           break;
                       case "last_name":
                           reader[1] = (readCharacters(library));
                           break;
                       case "date_of_birth":
                           reader[2] = (readCharacters(library));
                           break;
                       case "phone_number":
                           reader[3] = (readCharacters(library));
                           break;
                       case "email":
                           reader[4] = (readCharacters(library));
                           break;
                       case "reader_id":
                           reader[5] = (readCharacters(library));
                           break;
                   }
                   break;
               case XMLStreamReader.END_ELEMENT:
                   return new Reader(reader[0],reader[1], LocalDate.parse(reader[2]),reader[3],reader[4],reader[5]);
           }
       }
       throw new XMLStreamException("Premature end of file");
   }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. wypożyczeń.
     * @param library XMLStream
     * @return lista wypożyczeń
     * @throws XMLStreamException wyjątek XMLStream
     */
    private List<Rent> readRents(XMLStreamReader library) throws XMLStreamException {
        List<Rent> rents = new ArrayList<>();
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType){
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("rent"))
                        rents.add(readRent(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return rents;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. wypożyczenia.
     * @param library XMLStream
     * @return obiekt klasy Rent
     * @throws XMLStreamException wyjątek XMLStream
     */
    private Rent readRent(XMLStreamReader library) throws XMLStreamException {
        String[] rent = new String[6];
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    switch (elementName) {
                        case "book_id":
                            rent[0] = (readCharacters(library));
                            break;
                        case "reader_id":
                            rent[1] = (readCharacters(library));
                            break;
                        case "date_of_rent":
                            rent[2] = (readCharacters(library));
                            break;
                        case "date_of_return":
                            rent[3] = (readCharacters(library));
                            break;
                        case "returned":
                            rent[4] = (readCharacters(library));
                            break;
                        case "rent_id":
                            rent[5] = (readCharacters(library));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                   return new Rent(Library.findBookById(books,rent[0]),Library.findReaderById(readers,rent[1]),
                           LocalDateTime.parse(rent[2]),LocalDateTime.parse(rent[3]),Boolean.parseBoolean(rent[4]),rent[5]);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. przypomnień.
     * @param library XMLStream
     * @return lista przypomnień
     * @throws XMLStreamException wyjątek XMLStream
     */
    private List<RentalReminder> readRentalReminders(XMLStreamReader library) throws XMLStreamException {
        List<RentalReminder> rentalReminders = new ArrayList<>();
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType){
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("rentalReminder"))
                        rentalReminders.add(readRentalReminder(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return rentalReminders;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca kolejne elementy z pliku XML dot. przypomnienia.
     * @param library XMLStream
     * @return obiekt klasy RentalReminder
     * @throws XMLStreamException wyjątek XMLStream
     */
    private RentalReminder readRentalReminder(XMLStreamReader library) throws XMLStreamException {
        String[] rentalReminder = new String[3];
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    switch (elementName) {
                        case "rent_id":
                            rentalReminder[0] = (readCharacters(library));
                            break;
                        case "date_of_reminder":
                            rentalReminder[1] = (readCharacters(library));
                            break;
                        case "rentalreminder_id":
                            rentalReminder[2] = (readCharacters(library));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return new RentalReminder(Library.findRentById(rents,rentalReminder[0]),
                            LocalDateTime.parse(rentalReminder[1]),rentalReminder[2]);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Metoda odczytująca znaki z poszczególnego znacznika XML.
     * @param reader XMLStream
     * @return ciąg znaków odczytany z poszczególnego znacznika
     * @throws XMLStreamException wyjątek XMLStream
     */
    private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

}
