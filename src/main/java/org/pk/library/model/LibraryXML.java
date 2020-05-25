package org.pk.library.model;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibraryXML {

    public void saveDataToXML(Library library, Path filePath) throws IOException, XMLStreamException {
        try(OutputStream os = Files.newOutputStream((filePath))) {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = null;
            try{
                writer = outputFactory.createXMLStreamWriter(os,"utf-8");
                writeLibraryToXML(writer, library);
            }finally {
                if(writer != null)
                    writer.close();
            }
        }
    }

    private void writeLibraryToXML(XMLStreamWriter writer, Library library) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeProcessingInstruction("xml-stylesheet", "type=\"text/css\" href=\"arkusz_stylow.css\"");
        writer.writeCharacters("\n");
        writer.writeStartElement("library");
        writer.writeCharacters("\n\t");
        writer.writeStartElement("books");
        writer.writeCharacters("\n");
        for (Book book : library.getBooks())
            writeBookElem(writer, book);
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
        writer.writeStartElement("readers");
        writer.writeCharacters("\n");
        for (Reader reader: library.getReaders())
            writeReaderElem(writer,reader);
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
        writer.writeStartElement("rents");
        writer.writeCharacters("\n");
        for(Rent rent: library.getRents())
            writeRentElem(writer,rent);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndDocument();
    }

    private void writeBookElem(XMLStreamWriter writer, Book book) throws XMLStreamException {
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

    private void writeReaderElem(XMLStreamWriter writer, Reader reader) throws XMLStreamException {
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

    private void writeRentElem(XMLStreamWriter writer, Rent rent) throws XMLStreamException {
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
        ;
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

    public Library readDataFromXML(InputStream is) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader library = null;
        try {
            library = inputFactory.createXMLStreamReader(is);
            return readDocument(library);
        } finally {
            if(library != null) {
                library.close();
            }
        }
    }

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

    private Library readLibrary(XMLStreamReader library) throws XMLStreamException {
        List<Book> books = null;
        List<Reader> readers= null;
        List<Rent> rents =null;
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("books"))
                        books = readBooks(library);
                    if(elementName.equals("readers"))
                        readers =  readReaders(library);
                    if(elementName.equals("rents"))
                        rents =  readRents(library,books,readers);
                   break;
                case XMLStreamReader.END_ELEMENT:
                    return new Library(books,readers,rents);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

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

    private Book readBook(XMLStreamReader library) throws XMLStreamException {
        //Book book;
        String[] book = new String[5];
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("isbn"))
                        book[0] = (readCharacters(library));
                    else if(elementName.equals("title"))
                        book[1] = (readCharacters(library));
                    else if(elementName.equals("author"))
                        book[2] = (readCharacters(library));
                    else if(elementName.equals("publisher"))
                        book[3] = (readCharacters(library));
                    else if(elementName.equals("book_id"))
                        book[4] = (readCharacters(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return new Book(book[0],book[1],book[2],book[3],book[4]);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

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

   private Reader readReader(XMLStreamReader library) throws XMLStreamException {
       String[] reader = new String[6];
       while(library.hasNext()) {
           int eventType = library.next();
           switch (eventType) {
               case XMLStreamReader.START_ELEMENT:
                   String elementName = library.getLocalName();
                   if(elementName.equals("first_name"))
                       reader[0] = (readCharacters(library));
                   else if(elementName.equals("last_name"))
                       reader[1] = (readCharacters(library));
                   else if(elementName.equals("date_of_birth"))
                       reader[2] = (readCharacters(library));
                   else if(elementName.equals("phone_number"))
                       reader[3] = (readCharacters(library));
                   else if(elementName.equals("email"))
                       reader[4] = (readCharacters(library));
                   else if(elementName.equals("reader_id"))
                       reader[5] = (readCharacters(library));
                   break;
               case XMLStreamReader.END_ELEMENT:
                   return new Reader(reader[0],reader[1], LocalDate.parse(reader[2]),reader[3],reader[4],reader[5]);
           }
       }
       throw new XMLStreamException("Premature end of file");
   }

    private List<Rent> readRents(XMLStreamReader library,List<Book> books, List<Reader> readers) throws XMLStreamException {
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

    private Rent readRent(XMLStreamReader library) throws XMLStreamException {
        String[] rent = new String[6];
        while(library.hasNext()) {
            int eventType = library.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = library.getLocalName();
                    if(elementName.equals("book_id"))
                        rent[0] = (readCharacters(library));
                    else if(elementName.equals("reader_id"))
                        rent[1] = (readCharacters(library));
                    else if(elementName.equals("date_of_rent"))
                        rent[2] = (readCharacters(library));
                    else if(elementName.equals("date_of_return"))
                        rent[3] = (readCharacters(library));
                    else if(elementName.equals("returned"))
                        rent[4] = (readCharacters(library));
                    else if(elementName.equals("rent_id"))
                        rent[5] = (readCharacters(library));
                    break;
                case XMLStreamReader.END_ELEMENT:
                   return new Rent(null,null, LocalDateTime.parse(rent[2]),LocalDateTime.parse(rent[3]),Boolean.parseBoolean(rent[4]),rent[5]);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

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
