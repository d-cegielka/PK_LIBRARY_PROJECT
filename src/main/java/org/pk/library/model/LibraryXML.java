package org.pk.library.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryXML {

    public void saveDataToXML(Library library, String filePath) throws IOException {
        XStream stream = new XStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        ObjectOutputStream outputStream = stream.createObjectOutputStream(writer);
        outputStream.writeObject(library);
        outputStream.close();
        writer.close();
       /* XMLEncoder encoder = null;
        encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)));
        List<Book> books = new ArrayList<>(library.getBooks());
        System.out.println(books);
        encoder.writeObject(books);
        //encoder.writeObject(library.getReaders());
        //encoder.writeObject(library.getRents());
        //encoder.writeObject(library.getRentalReminders());
        //encoder.writeObject(library);
        encoder.close();*/

    }

    public Library readDataFromXML(String filePath) throws IOException, ClassNotFoundException {
        XStream stream = new XStream(new DomDriver());
        BufferedReader inputFile = new BufferedReader(new FileReader(new File(filePath)));
        ObjectInputStream inputObject = stream.createObjectInputStream(inputFile);
        return (Library) inputObject.readObject();

        /*XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
        return (Library) decoder.readObject();*/
    }

}
