package org.pk.library.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.pk.library.model.Reader;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Kontroler GUI obsługujący żądania dotyczące czytelników.
 */
public class ReaderController {
    private MainController mainController;
    @FXML
    JFXTextField findReaderField;
    @FXML
    private JFXTextField firstNameAddField;
    @FXML
    private JFXTextField lastNameAddField;
    @FXML
    private JFXDatePicker dateOfBirthAddField;
    @FXML
    private JFXTextField phoneNumberAddField;
    @FXML
    private JFXTextField emailAddressAddField;
    @FXML
    private JFXTextField firstNameUpdateField;
    @FXML
    private JFXTextField lastNameUpdateField;
    @FXML
    private JFXDatePicker dateOfBirthUpdateField;
    @FXML
    private JFXTextField phoneNumberUpdateField;
    @FXML
    private JFXTextField emailAddressUpdateField;
    @FXML
    private JFXButton deleteReaderButton;
    @FXML
    private JFXTreeTableView<Reader> readersTableView;
    @FXML
    private JFXTreeTableColumn<Reader, String> firstNameCol;
    @FXML
    private JFXTreeTableColumn<Reader, String> lastNameCol;
    @FXML
    private JFXTreeTableColumn<Reader, LocalDate> dateOfBirthCol;
    @FXML
    private JFXTreeTableColumn<Reader, String> phoneNumberCol;
    @FXML
    private JFXTreeTableColumn<Reader, String> emailAddressCol;

    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeReaderTableView();
    }

    /**
     * Inicializacja kolumn w tabeli z listą czytelników oraz wyszukiwarki czytelników
     */
    public void initializeReaderTableView(){

        firstNameCol = new JFXTreeTableColumn<>("Imię");
        firstNameCol.prefWidthProperty().bind(readersTableView.widthProperty().subtract(20).divide(5));
        firstNameCol.setResizable(false);
        firstNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(firstNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getFirstName());
            else return firstNameCol.getComputedValue(param);
        });

        lastNameCol = new JFXTreeTableColumn<>("Nazwisko");
        lastNameCol.prefWidthProperty().bind(readersTableView.widthProperty().subtract(20).divide(5));
        lastNameCol.setResizable(false);
        lastNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(lastNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getLastName());
            else return lastNameCol.getComputedValue(param);
        });

        dateOfBirthCol = new JFXTreeTableColumn<>("Data urodzenia");
        dateOfBirthCol.prefWidthProperty().bind(readersTableView.widthProperty().subtract(20).divide(5));
        dateOfBirthCol.setResizable(false);
        dateOfBirthCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, LocalDate> param) ->{
            if(dateOfBirthCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfBirth());
            else return dateOfBirthCol.getComputedValue(param);
        });

        phoneNumberCol = new JFXTreeTableColumn<>("Tel. kontaktowy");
        phoneNumberCol.prefWidthProperty().bind(readersTableView.widthProperty().subtract(20).divide(5));
        phoneNumberCol.setResizable(false);
        phoneNumberCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(phoneNumberCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getPhoneNumber());
            else return phoneNumberCol.getComputedValue(param);
        });

        emailAddressCol = new JFXTreeTableColumn<>("Email");
        emailAddressCol.prefWidthProperty().bind(readersTableView.widthProperty().subtract(20).divide(5));
        emailAddressCol.setResizable(false);
        emailAddressCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(emailAddressCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEmailAddress());
            else return emailAddressCol.getComputedValue(param);
        });

        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getReaders()), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);

        readersTableView.setShowRoot(false);
        readersTableView.setEditable(true);
        readersTableView.getColumns().setAll(firstNameCol, lastNameCol, dateOfBirthCol, phoneNumberCol, emailAddressCol);

        findReaderField.textProperty().addListener((o, oldVal, newVal) -> readersTableView.setPredicate(readerProp -> {
            final Reader reader = readerProp.getValue();
            String checkValue = newVal.trim().toLowerCase();
            return reader.toString().toLowerCase().contains(checkValue);
        }));

        readersTableView.currentItemsCountProperty().addListener((observableValue, rentTreeItem, t1) -> {
            if (readersTableView.getCurrentItemsCount() == 0) clearUpdateReaderForm();
        });
        readersTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, rentTreeItem, t1) -> changeUpdateReaderForm());
    }

    /**
     * Aktualizacja listy czytelników
     */
    @FXML
    void reloadReaderTableView(){
        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getReaders()), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);
    }

    /**
     * Dodawanie czytelnika do struktury danych
     */
    @FXML
    private void addReader() {
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.addReader(
                        firstNameAddField.getText(),
                        lastNameAddField.getText(),
                        dateOfBirthAddField.getValue(),
                        phoneNumberAddField.getText(),
                        emailAddressAddField.getText()
                )
        );
        reloadReaderTableView();
    }

    /**
     * Aktualizacja czytelnika w strukturze danych
     */
    @FXML
    private void updateReader() {
        int readerIndex = readersTableView.getSelectionModel().getSelectedIndex();
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.updateReader(
                        readersTableView.getSelectionModel().getSelectedItem().getValue(),
                        firstNameUpdateField.getText(),
                        lastNameUpdateField.getText(),
                        dateOfBirthUpdateField.getValue(),
                        phoneNumberUpdateField.getText(),
                        emailAddressUpdateField.getText()
                )
        );
        reloadReaderTableView();
        readersTableView.getSelectionModel().select(readerIndex);
    }

    /**
     * Usuwanie czytelnika z struktury danych
     */
    @FXML
    private void deleteReader() {
        final Reader readerToRemove = readersTableView.getSelectionModel().getSelectedItem().getValue();
        if(confirmDeletionReader(readerToRemove.getFirstName(), readerToRemove.getLastName())){
            mainController.showInfoDialog(
                    "Informacja",
                    mainController.libraryController.deleteReader(readerToRemove)
            );
        }
        reloadReaderTableView();
    }

    /**
     * Aktualizacja danych formularza po wybraniu czytelnika z listy
     */
    private void changeUpdateReaderForm() {
        if(readersTableView.getSelectionModel().getSelectedItem() != null) {
            firstNameUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getFirstName());
            lastNameUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getLastName());
            dateOfBirthUpdateField.setValue(readersTableView.getSelectionModel().getSelectedItem().getValue().getDateOfBirth());
            phoneNumberUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getPhoneNumber());
            emailAddressUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getEmailAddress());
            deleteReaderButton.setDisable(false);
        }
    }

    /**
     * Metoda wypisująca inforamcję w oknie dialogowym podczas usuwania książki
     * @param firstName imię czytelnika
     * @param lastName nazwisko czytelnika
     * @return potwierdzenie/niepotwierdzenie usunięcia
     */
    @FXML
    boolean confirmDeletionReader(String firstName, String lastName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdź wybór");
        alert.setHeaderText("Czy na pewno chcesz usunąć czytelnika " + firstName + " " + lastName+" ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;
    }

    /**
     * Czyszczenie pól formularza dodawania czytelnika
     */
    @FXML
    private void clearAddReaderForm() {
        firstNameAddField.clear();
        lastNameAddField.clear();
        dateOfBirthAddField.setValue(null);
        phoneNumberAddField.clear();
        emailAddressAddField.clear();
    }

    /**
     * Czyszczenie pól formularza aktualizacji danych czytelnika
     */
    @FXML
    private void clearUpdateReaderForm() {
        firstNameUpdateField.clear();
        lastNameUpdateField.clear();
        dateOfBirthUpdateField.setValue(null);
        phoneNumberUpdateField.clear();
        emailAddressUpdateField.clear();
        deleteReaderButton.setDisable(true);
    }

}
