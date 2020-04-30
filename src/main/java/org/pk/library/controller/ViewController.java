package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

//Usunieto @FXML i FXML w razie problemow dodac

public class ViewController {
    public AnchorPane roots;
    public TabPane tabPane;
    Controller controller;
    public StackPane mainStackPane;
    public JFXTextField findBookField;
    public JFXTreeTableView<?> booksTableView;
    public JFXTextField bookTitleAddField;
    public JFXTextField bookIsbnAddField;
    public JFXTextField bookAuthorAddField;
    public JFXTextField bookPublisherAddField;
    public JFXTextField bookTitleUpdateField;
    public JFXTextField bookIsbnUpdateField;
    public JFXTextField bookAuthorUpdateField;
    public JFXTextField bookPublisherUpdateField;

    @FXML
    public void initialize() {
        controller = new Controller();
    }

    @FXML
    public void addBook(ActionEvent actionEvent) {
        try {
            controller.addBook(bookIsbnAddField.getText().trim(),
                    bookTitleAddField.getText().trim(),
                    bookAuthorAddField.getText().trim(),
                    bookPublisherAddField.getText().trim());
            showInfoDialog("Informacja", "Książka została dodana pomyślnie!");
        } catch (Exception e) {
            showInfoDialog("Sprawdzenie formularza",e.getMessage());
        }
    }

    public void clearAddBookForm(ActionEvent actionEvent) {
        bookTitleAddField.clear();
        bookIsbnAddField.clear();
        bookAuthorAddField.clear();
        bookPublisherAddField.clear();
    }

    public void updateBook(ActionEvent actionEvent) {
    }

    public void clearUpdateBookForm(ActionEvent actionEvent) {
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


