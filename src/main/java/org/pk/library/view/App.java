package org.pk.library.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.pk.library.model.*;
import org.pk.library.model.Reader;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"));
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setScene(scene);
        stage.setTitle("Biblioteka");
        stage.getIcons().add(new Image(String.valueOf(App.class.getResource("icon.png"))));
        stage.show();
    }

   public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static final String BOOKSTORE_XML = "bookstore-jaxb.xml";

    public static void main(String[] args){
   /*     if(args.length > 0 && args[0].equals("no-gui")) {
            System.out.println("NOGUI");
        } else{
            launch();
        }*/
        System.out.println("test czy cos wypisze");
       // List<Book> books = new ArrayList<>();
        Library library = new Library();
        Book book1, book2, book3;
        Reader reader1, reader2;
        Rent rent1;

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

        LibraryXML libraryXML = new LibraryXML();
        File xmlOutFile = new File("library.xml");
        Path xmlOutFilePath = Paths.get(xmlOutFile.getAbsolutePath());
        Path outFile = null;

        try(InputStream is = new FileInputStream(new File("library.xml"))) {
            if(is == null)
                throw new IOException("Failed to open resource");
            LibraryXML libraryXML1 = new LibraryXML();
            System.out.println(libraryXML1.readDataFromXML(is));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    /*    try {
        Files.deleteIfExists(xmlOutFilePath);
        outFile = Files.createFile(xmlOutFilePath);
        libraryXML.saveDataToXML(library,outFile);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        System.out.println("Pomyślnie zapisano");*/

       /* Library library;
        LibraryXML libraryXML;
        Book book1, book2, book3;
        Reader reader1, reader2;
        Rent rent1;
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
        libraryXML = new LibraryXML();
        try {
            libraryXML.saveDataToXML(library,"library.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}