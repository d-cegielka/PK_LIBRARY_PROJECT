package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.pk.library.model.Book;
import org.pk.library.model.Library;

//Usunieto @FXML i FXML w razie problemow dodac

public class MainController {
    public AnchorPane roots;
    public TabPane tabPane;
    public StackPane mainStackPane;
    Library library;


    @FXML
    private BookController bookController;

    @FXML
    private void initialize() {
        bookController.injectMainController(this);
        library = new Library();
        Book book1;
        book1 = new Book("5869782","Harry Potter", "J.K Rowling","Bloomsbury Publishing");
        library.addBook(book1);
        bookController.initializeBookTableViewColumn();
    }

/*    public void addReader(ActionEvent actionEvent) {
        try {
            controller.addReader(bookIsbnAddField.getText(),
                    bookTitleAddField.getText(),
                    bookAuthorAddField.getText(),
                    bookPublisherAddField.getText());
            showInfoDialog("Informacja", "Czytelnik został dodany pomyślnie!");
        } catch (Exception e) {
            showInfoDialog("Sprawdzenie formularza",e.getMessage());
        }
    }*/

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
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            tabPane.setEffect(null);
        });
        tabPane.setEffect(blur);

    }
}


