package org.pk.library.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.pk.library.controller.Controller;

import java.sql.SQLException;

public class MainController {
    public JFXToggleButton changeStyleToggleButton;
    Controller libraryController;
    @FXML
    BookController bookController;
    @FXML
    ReaderController readerController;
    @FXML
    RentController rentController;
    @FXML
    ReturnController returnController;
    @FXML
    CalendarController calendarController;

    @FXML
    private AnchorPane mainPane;
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
        rentController.injectMainController(this);
        returnController.injectMainController(this);
        calendarController.injectMainController(this);
    }

    /**
     * Metoda wypisująca informację w oknie dialogowym w przypadku powodzenia lub niepowodzenia.
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
    void reloadRentView(){
        rentController.reloadRentTableView();
        rentController.reloadReadersTableView();
        rentController.reloadBooksTableView();
    }

    public void changeStyle(ActionEvent actionEvent) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500),mainPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.05);
        fadeTransition.play();

        fadeTransition.setOnFinished((ActionEvent event) -> {
            if(changeStyleToggleButton.isSelected()) {
                mainPane.getStylesheets().clear();
                mainPane.getStylesheets().add(String.valueOf(getClass().getResource("light-theme.css")));
            } else {
                mainPane.getStylesheets().clear();
                mainPane.getStylesheets().add(String.valueOf(getClass().getResource("dark-theme.css")));
            }
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500),mainPane);
            fadeOut.setFromValue(0.05);
            fadeOut.setToValue(1);
            fadeOut.play();
        });

    }
}


