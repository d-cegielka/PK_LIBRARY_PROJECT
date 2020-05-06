package org.pk.library.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import org.pk.library.model.Reader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ReaderController {

    private MainController mainController;
    public JFXTextField findReaderField;
    public JFXTextField firstNameAddField;
    public JFXTextField lastNameAddField;
    public JFXDatePicker dateOfBirthAddField;
    public JFXTextField phoneNumberAddField;
    public JFXTextField emailAddressAddField;
    public JFXTextField firstNameUpdateField;
    public JFXTextField lastNameUpdateField;
    public JFXDatePicker dateOfBirthUpdateField;
    public JFXTextField phoneNumberUpdateField;
    public JFXTextField emailAddressUpdateField;
    LocalDate today = LocalDate.now();
    @FXML
    JFXTreeTableView<Reader> readersTableView;
    JFXTreeTableColumn<Reader,String> firstNameCol;
    JFXTreeTableColumn<Reader,String> lastNameCol;
    JFXTreeTableColumn<Reader, LocalDate> dateOfBirthCol;
    JFXTreeTableColumn<Reader,String> phoneNumberCol;
    JFXTreeTableColumn<Reader,String> emailAddressCol;
    Pattern phoneNumberPattern;
    Pattern emailAddressPattern;


    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        phoneNumberPattern = Pattern.compile("^\\+[0-9]{1,3}[0-9]{4,14}(?:x.+)?$");
        emailAddressPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    /**
     * Inicializacja kolumn w tabeli z listą czytelników oraz wyszukiwarka czytelników
     */
    public void initializeReaderTableView(){

        firstNameCol = new JFXTreeTableColumn<>("Imię");
        firstNameCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(5));
        firstNameCol.setResizable(false);
        firstNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(firstNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getFirstName());
            else return firstNameCol.getComputedValue(param);
        });

        lastNameCol = new JFXTreeTableColumn<>("Nazwisko");
        lastNameCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(5));
        lastNameCol.setResizable(false);
        lastNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(lastNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getLastName());
            else return lastNameCol.getComputedValue(param);
        });

        dateOfBirthCol = new JFXTreeTableColumn<>("Data urodzenia");
        dateOfBirthCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(5));
        dateOfBirthCol.setResizable(false);
        dateOfBirthCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, LocalDate> param) ->{
            if(dateOfBirthCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfBirth());
            else return dateOfBirthCol.getComputedValue(param);
        });

        phoneNumberCol = new JFXTreeTableColumn<>("Tel. kontaktowy");
        phoneNumberCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(5));
        phoneNumberCol.setResizable(false);
        phoneNumberCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(phoneNumberCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getPhoneNumber());
            else return phoneNumberCol.getComputedValue(param);
        });

        emailAddressCol = new JFXTreeTableColumn<>("Email");
        emailAddressCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(5));
        emailAddressCol.setResizable(false);
        emailAddressCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(emailAddressCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEmailAddress());
            else return emailAddressCol.getComputedValue(param);
        });

        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(mainController.library.getReaders(), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);

        readersTableView.setShowRoot(false);
        readersTableView.setEditable(true);
        readersTableView.getColumns().setAll(firstNameCol, lastNameCol, dateOfBirthCol, phoneNumberCol, emailAddressCol);

        findReaderField.textProperty().addListener((o, oldVal, newVal) -> {
            readersTableView.setPredicate(readerProp -> {
                clearUpdateReaderForm();
                final Reader reader = readerProp.getValue();
                String checkValue = newVal.trim().toLowerCase();
                return (reader.getFirstName().toLowerCase().contains(checkValue) ||
                        reader.getLastName().toLowerCase().contains(checkValue) ||
                        reader.getDateOfBirth().toString().contains(checkValue) ||
                        reader.getPhoneNumber().toLowerCase().contains(checkValue) ||
                        reader.getEmailAddress().toLowerCase().contains(checkValue));
            });
        });

        readersTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> changeUpdateReaderForm());
    }

    /**
     * Aktualizacja listy czytelników
     */
    private void reloadReaderTableView(){
        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(mainController.library.getReaders(), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);
    }

    /**
     * Dodawanie czytelnika do struktury danych
     */
    @FXML
    private void addReader() {
        Reader readerToAdd = null;

        try {
            if(firstNameAddField.getText().trim().isEmpty() || lastNameAddField.getText().trim().isEmpty() ||
                    dateOfBirthAddField.getValue() == null || phoneNumberAddField.getText().trim().isEmpty() ||
                    emailAddressAddField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Uzupełnij wszystkie pola!");
            }
            if(dateOfBirthAddField.getValue().isAfter(today)) {
                throw new IllegalArgumentException("Wybrano datę urodzenia z przyszłości! Popraw dane!");
            }
            if(!(phoneNumberPattern.matcher(phoneNumberAddField.getText().trim())).matches()) {
                throw new IllegalArgumentException("Niepoprawny format numeru kontaktowego!");
            }
            if(!(emailAddressPattern.matcher(emailAddressAddField.getText().trim())).matches()) {
                throw new IllegalArgumentException("Niepoprawny format adresu e-mail!");
            }

            readerToAdd = new Reader(firstNameAddField.getText().trim(), lastNameAddField.getText().trim(),
                    dateOfBirthAddField.getValue(), phoneNumberAddField.getText().trim(), emailAddressAddField.getText().trim());

            if(!mainController.library.addReader(readerToAdd))  {
                throw new Exception("Nie udało się dodać czytelnika!");
            }
            if(mainController.libraryDB != null) {
                mainController.libraryDB.insertReader(readerToAdd);
            }

            mainController.showInfoDialog("Informacja", "Czytelnik został dodany pomyślnie!");
        } catch (SQLException se){
            mainController.library.removeReader(readerToAdd);
            mainController.showInfoDialog("Informacja SQL",se.getMessage());
        } catch (IllegalArgumentException iae) {
            mainController.showInfoDialog("Sprawdzenie formularza",iae.getMessage());
        } catch (Exception e) {
            mainController.showInfoDialog("Informacja",e.getMessage());
        }
        reloadReaderTableView();
    }

    /**
     * Aktualizacja czytelnika w strukturze danych
     */
    @FXML
    private void updateReader() {
        try {
            int changesNum = 0;
            if(firstNameUpdateField.getText().trim().isEmpty() || lastNameUpdateField.getText().trim().isEmpty() ||
                    dateOfBirthUpdateField.getValue() == null || phoneNumberUpdateField.getText().trim().isEmpty() ||
                    emailAddressUpdateField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Uzupełnij wszystkie pola!");
            }
            if (!firstNameUpdateField.getText().trim().equals(readersTableView.getSelectionModel().getSelectedItem().getValue().getFirstName())) {
                readersTableView.getSelectionModel().getSelectedItem().getValue().setFirstName(firstNameUpdateField.getText());
                changesNum++;
            }
            if (!lastNameUpdateField.getText().trim().equals(readersTableView.getSelectionModel().getSelectedItem().getValue().getLastName())) {
                readersTableView.getSelectionModel().getSelectedItem().getValue().setLastName(lastNameUpdateField.getText().trim());
                changesNum++;
            }
            if (!dateOfBirthUpdateField.getValue().equals(readersTableView.getSelectionModel().getSelectedItem().getValue().getDateOfBirth())) {
                if(dateOfBirthUpdateField.getValue().isAfter(today)) {
                    throw new IllegalArgumentException("Wybrano datę urodzenia z przyszłości! Popraw dane!");
                }
                readersTableView.getSelectionModel().getSelectedItem().getValue().setDateOfBirth(dateOfBirthUpdateField.getValue());
                changesNum++;
            }
            if (!phoneNumberUpdateField.getText().trim().equals(readersTableView.getSelectionModel().getSelectedItem().getValue().getPhoneNumber())) {
                if(!(phoneNumberPattern.matcher(phoneNumberUpdateField.getText().trim())).matches()) {
                    throw new IllegalArgumentException("Niepoprawny format numeru kontaktowego!");
                }
                readersTableView.getSelectionModel().getSelectedItem().getValue().setPhoneNumber(phoneNumberUpdateField.getText().trim());
                changesNum++;
            }
            if (!emailAddressUpdateField.getText().trim().equals(readersTableView.getSelectionModel().getSelectedItem().getValue().getEmailAddress())) {
                if(!(emailAddressPattern.matcher(emailAddressUpdateField.getText().trim())).matches()) {
                    throw new IllegalArgumentException("Niepoprawny format adresu e-mail");
                }
                readersTableView.getSelectionModel().getSelectedItem().getValue().setEmailAddress(emailAddressUpdateField.getText().trim());
                changesNum++;
            }

            if(changesNum > 0) {
                mainController.libraryDB.updateReader(readersTableView.getSelectionModel().getSelectedItem().getValue());
                mainController.showInfoDialog("Informacja", "Dane czytelnika zostały zaktualizowane pomyślnie!\n" +
                        "Ilość wprowadzonych zmian: " + changesNum);
                reloadReaderTableView();
            } else {
                mainController.showInfoDialog("Informacja", "Nie wprowadzono żadnych zmian!");
            }

        } catch (IllegalArgumentException e) {
            mainController.showInfoDialog("Sprawdzenie formularza",e.getMessage());
        } catch (SQLException se) {
            mainController.showInfoDialog("Informacja SQL",se.getMessage());
        }

    }

    /**
     * Usuwanie czytelnika z struktury danych
     */
    @FXML
    private void deleteReader() {
        final Reader readerToRemove = readersTableView.getSelectionModel().getSelectedItem().getValue();
        try {
            if(mainController.confirmDeletionReader(readerToRemove.getFirstName(), readerToRemove.getLastName())){
                if(mainController.libraryDB.deleteFromTable("READERS",readerToRemove.getREADER_ID()) && mainController.library.removeReader(readerToRemove)){
                    mainController.showInfoDialog("Informacja", "Czytelnik został usunięty pomyślnie!");
                }
                else {
                    mainController.showInfoDialog("Informacja", "Czytelnik nie został usunięty!");
                }
            }
        } catch (SQLException se) {
            mainController.showInfoDialog("Informacja SQL",se.getMessage());
        }
        reloadReaderTableView();
    }


    /**
     * Aktualizacja danych formularza po wybraniu czytelnika z listy
     */
    private void changeUpdateReaderForm() {
        clearUpdateReaderForm();
       if(!readersTableView.getSelectionModel().isEmpty()) {
            firstNameUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getFirstName());
            lastNameUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getLastName());
            dateOfBirthUpdateField.setValue(readersTableView.getSelectionModel().getSelectedItem().getValue().getDateOfBirth());
            phoneNumberUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getPhoneNumber());
            emailAddressUpdateField.setText(readersTableView.getSelectionModel().getSelectedItem().getValue().getEmailAddress());
        }
    }

    /**
     * Czyszczenie pól formularza dodawania czytelnika
     */
    @FXML
    public void clearAddReaderForm() {
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
    public void clearUpdateReaderForm() {
        firstNameUpdateField.clear();
        lastNameUpdateField.clear();
        dateOfBirthUpdateField.setValue(null);
        phoneNumberUpdateField.clear();
        emailAddressUpdateField.clear();
    }

}
