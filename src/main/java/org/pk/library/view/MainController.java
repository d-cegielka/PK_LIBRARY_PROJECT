package org.pk.library.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.pk.library.controller.Controller;
import org.pk.library.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    Controller libraryController;
    @FXML
    private BookController bookController;
    @FXML
    private ReaderController readerController;
    @FXML
    private TabPane tabPane;
    @FXML
    private StackPane mainStackPane;

    @FXML
    private void initialize() {
        try {
            libraryController = new Controller();
        } catch (SQLException se) {
            showInfoDialog("Inicjalizacja kontrolera biblioteki",se.getMessage());
        }

        bookController.injectMainController(this);
        readerController.injectMainController(this);

        bookController.initializeBookTableView();
        readerController.initializeReaderTableView();
    }

    /**
     * Metoda wypisująca inforamcję w oknie dialogowym w przypadku powodzenia lub niepowodzenia
     * @param header nagłówek informacji
     * @param message treść informacji
     */
    @FXML
    void showInfoDialog(String header, String message){
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

    @FXML
    public boolean confirmDeletionBook(String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdź wybór");
        alert.setHeaderText("Czy na pewno chcesz usunąć książkę pt. \"" + title + "\" ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @FXML
    public boolean confirmDeletionReader(String firstName, String lastName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdź wybór");
        alert.setHeaderText("Czy na pewno chcesz usunąć czytelnika " + firstName + " " + lastName+" ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}


