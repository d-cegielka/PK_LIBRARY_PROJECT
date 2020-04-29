package org.pk.library.controller;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
//Usunieto @FXML i FXML w razie problemow dodac
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ViewController {
    public AnchorPane roots;
    Controller controller;
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
            controller.addBook(bookIsbnAddField.getText(),
                    bookTitleAddField.getText(),
                    bookAuthorAddField.getText(),
                    bookPublisherAddField.getText());
            showInfoDialog("Test","DIX");
        } catch (Exception e) {
            showInfoDialog("Test",e.toString());
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

    @FXML
    public void showInfoDialog(String header, String message){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(header));
        dialogLayout.setBody(new Text(message));
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialogLayout.setActions(button);
        Scene scene = new Scene(roots,300,250);
        Stage tempstage = (Stage) roots.getScene().getWindow();
        tempstage.setScene(scene);
        dialog.show();
        tempstage.show();

       BoxBlur boxBlur = new BoxBlur(3,3,3);


    }
}


