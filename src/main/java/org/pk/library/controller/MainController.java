package org.pk.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pk.library.model.Library;
import org.pk.library.model.LibraryDB;

import java.sql.SQLException;

public class MainController {
    Library library;
    LibraryDB libraryDB;

    public MainController() throws SQLException {
       // ObservableList<?> test = FXCollections.observableArrayList(?);
        library = new Library();
        libraryDB = new LibraryDB();
    }
}
