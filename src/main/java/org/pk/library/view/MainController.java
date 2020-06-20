package org.pk.library.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pk.library.controller.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Główny kontroler GUI.
 */
public class MainController {
    Controller libraryController;
    Settings appSettings;
    @FXML
    BookController bookController;
    @FXML
    ReaderController readerController;
    @FXML
    RentController rentController;
    @FXML
    ReturnController returnController;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private JFXToggleButton changeStyleToggleButton;

    @FXML
    private void initialize() {
        try {
            libraryController = new Controller(true);
        } catch (SQLException | IOException se) {
            System.out.println(se.getMessage());
            Platform.runLater(() -> showInfoDialog("Brak połączenia z bazą",
                    "Program nie nawiązał połączenia z bazą danych! Komunikat:\n" + se.getMessage()
                            +"\n\nAby zachować dane programu zapisz je do pliku XML (Plik -> Eksportuj do XML) przed zamknięciem aplikacji!"));
            try {
                libraryController = new Controller(false);
            } catch (IOException | SQLException e) {
                System.out.println(se.getMessage());
            }
        }
        bookController.injectMainController(this);
        readerController.injectMainController(this);
        rentController.injectMainController(this);
        returnController.injectMainController(this);
        shortcutObserver();
        appSettings = Settings.getInstance();
        if(!appSettings.getDarkThemeProp()){
            changeStyleToggleButton.selectedProperty().set(true);
            changeStyle();
        }
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
        showDialog(blur, dialogLayout);
    }

    /**
     * Przeładowanie widoku wypożyczeń
     */
    @FXML
    void reloadRentView(){
        rentController.reloadRentTableView();
        rentController.reloadReadersTableView();
        rentController.reloadBooksTableView();
    }

    /**
     * Przeładowanie widoku programu
     */
    @FXML
    void reloadAllView(){
        bookController.reloadBookTableView();
        readerController.reloadReaderTableView();
        reloadRentView();
        returnController.loadRentsCalendar();
        returnController.loadRentRemindersCalendar();
    }

    /**
     * Zmiana stylu GUI
     */
    @FXML
    void changeStyle() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), mainPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.05);
        fadeTransition.play();

        fadeTransition.setOnFinished((ActionEvent event) -> {
            if(changeStyleToggleButton.isSelected()) {
                mainPane.getStylesheets().clear();
                mainPane.getStylesheets().add(String.valueOf(getClass().getResource("styles/light-theme.css")));
                appSettings.setDarkThemeProp(false);
            } else {
                mainPane.getStylesheets().clear();
                mainPane.getStylesheets().add(String.valueOf(getClass().getResource("styles/dark-theme.css")));
                appSettings.setDarkThemeProp(true);
            }
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), mainPane);
            fadeOut.setFromValue(0.05);
            fadeOut.setToValue(1);
            fadeOut.play();
        });
    }

    /**
     * Obserwator skrótu klawiszowego CTRL+F
     */
    @FXML
    void shortcutObserver(){
        mainPane.setOnKeyPressed(e -> {
            if(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY).match(e)){
                switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
                    case "Książki":
                        bookController.findBookField.requestFocus();
                        break;
                    case "Czytelnicy":
                        readerController.findReaderField.requestFocus();
                        break;
                    case "Wypożyczenia":
                        rentController.findRentField.requestFocus();
                        break;
                    case "Zwroty":
                        returnController.returnCalendarView.getSearchField().requestFocus();
                        break;
                }
            }
        });
    }

    /**
     * Zmiana ikony programu.
     */
    @FXML
    private void changeAppIcon() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj nową ikonkę programu");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik graficzny (*.png)","*.png"));
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null){
            appSettings.setIconPath(file.toPath().toUri().toString());
            showInfoDialog("Informacja","Nowa ikona została wczytana pomyślnie! \nŚcieżka: "+ file.getPath() +
                    "\nNowa ikona będzie widoczna po ponownym uruchomieniu programu.");
        }

    }

    /**
     * Eksport struktury danych aplikacji do pliku XML.
     */
    @FXML
    private void exportToXML(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik XML do zapisu");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik XML (*.xml)","*.xml"));
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null){
            showInfoDialog("Informacja", libraryController.exportDataToXML(file.getPath()));
        }

    }

    /**
     * Import danych aplikacji z pliku XML do struktury danych.
     */
    @FXML
    private void importFromXML(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik XML do zaimportowania");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik XML (*.xml)","*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdź wybór");
            alert.setHeaderText("UWAGA! \nCzy na pewno chcesz zaimportować dane z pliku XML? \nPoprzednie dane zostaną usunięte!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.orElse(null) == ButtonType.OK) {
                showInfoDialog("Informacja", libraryController.importDataFromXML(file.getPath()));
                reloadAllView();
            }
        }

    }

    /**
     * O programie.
     */
    @FXML
    private void about(){
        BoxBlur blur = new BoxBlur(4, 4, 4);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        Label label = new Label("    \t" + appSettings.getAppTitle().toUpperCase() + " v1.0");
        label.setTextFill(Color.color(0.2,0.6,0.8));
        label.getStyleClass().add("aboutHeading");
        ImageView imageView = new ImageView(appSettings.getIconPath());
        imageView.setFitWidth(64);
        imageView.setPreserveRatio(true);
        dialogLayout.setHeading(imageView,label);
        dialogLayout.setBody(new Text("Autorzy: \n Dominik Cegiełka 224478 \n Kamil Zarych 224546 " +
                "\n\nSkróty klawiszowe: \n CTRL+F - wyszukiwanie obiektów \n TAB - zmiana aktywnego pola formularza "));
        showDialog(blur, dialogLayout);
    }

    /**
     * Metoda wyświetlając dialog z informacją.
     * @param blur wartość rozmycia tła
     * @param dialogLayout dialog do wyświetlenia
     */
    private void showDialog(BoxBlur blur, JFXDialogLayout dialogLayout) {
        JFXDialog dialog = new JFXDialog(mainStackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(
                event -> dialog.close());
        button.setButtonType(JFXButton.ButtonType.RAISED);
        dialogLayout.setActions(button);
        tabPane.setDisable(true);
        changeStyleToggleButton.setDisable(true);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            tabPane.setEffect(null);
            changeStyleToggleButton.setEffect(null);
            tabPane.setDisable(false);
            changeStyleToggleButton.setDisable(false);
        });
        tabPane.setEffect(blur);
        changeStyleToggleButton.setEffect(blur);
    }

    /**
     * Zamknij aplikacje.
     */
    @FXML
    private void quitApp() {
        Platform.exit();
    }
}


