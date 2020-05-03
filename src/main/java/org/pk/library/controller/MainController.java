package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.pk.library.model.*;

import java.sql.SQLException;

//Usunieto @FXML i FXML w razie problemow dodac

public class MainController {
    public AnchorPane roots;
    public TabPane tabPane;
    public StackPane mainStackPane;
    Library library;
    LibraryDB libraryDB;

    @FXML
    private BookController bookController;

    @FXML
    private ReaderController readerController;

    @FXML
    private void initialize() {
        library = new Library();
        bookController.injectMainController(this);
        readerController.injectMainController(this);
        try{
            libraryDB = new LibraryDB();
            ObservableList<Book> books = FXCollections.observableArrayList(libraryDB.getBooksFromDB());
            ObservableList<Reader> readers = FXCollections.observableArrayList(libraryDB.getReadersFromDB());
            ObservableList<Rent> rents = FXCollections.observableArrayList(libraryDB.getRentsFromDB());
            library = new Library(books,readers,rents);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        bookController.initializeBookTableView();
        readerController.initializeReaderTableView();

        //books = library.getBooks();
        //bookController.initializeBookTableViewColumn();
    }

    @FXML
    public void showInfoDialog(String header, String message){
        BoxBlur blur = new BoxBlur(4, 4, 4);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(header));
        dialogLayout.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton button = new JFXButton("Okay");
        //button.setStyle("-fx-background-color: green; -fx-text-fill: white");
        //button.getStyleClass().add("dialog-button");
        button.setOnAction(
                event -> dialog.close());
        button.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        dialogLayout.setActions(button);
        tabPane.setDisable(true);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            tabPane.setEffect(null);
            tabPane.setDisable(false);
        });
        tabPane.setEffect(blur);
    }
}


