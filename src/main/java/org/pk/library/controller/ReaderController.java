package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.pk.library.model.Book;
import org.pk.library.model.Reader;

import java.sql.Date;
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

    @FXML
    JFXTreeTableView<Reader> readersTableView;
    JFXTreeTableColumn<Reader,String> firstNameCol;
    JFXTreeTableColumn<Reader,String> lastNameCol;
    JFXTreeTableColumn<Reader, LocalDate> dateOfBirthCol;
    JFXTreeTableColumn<Reader,String> phoneNumberCol;
    JFXTreeTableColumn<Reader,String> emailAddressCol;
    Pattern phoneNumberPattern;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        phoneNumberPattern = Pattern.compile("^\\+[0-9]{1,3}[0-9]{4,14}(?:x.+)?$");
    }

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

        ObservableList<Reader> readerList = FXCollections.observableArrayList(mainController.library.getReaders());
        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(readerList, RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);

        readersTableView.setShowRoot(false);
        readersTableView.setEditable(true);
        readersTableView.getColumns().setAll(firstNameCol, lastNameCol, dateOfBirthCol, phoneNumberCol, emailAddressCol);

        findReaderField.textProperty().addListener((o, oldVal, newVal) -> {
            readersTableView.setPredicate(bookProp -> {
                final Reader reader = bookProp.getValue();
                String checkValue = newVal.trim().toLowerCase();
                return (reader.getFirstName().toLowerCase().contains(checkValue) ||
                        reader.getLastName().toLowerCase().contains(checkValue) ||
                        reader.getDateOfBirth().toString().contains(checkValue) ||
                        reader.getPhoneNumber().toLowerCase().contains(checkValue) ||
                        reader.getEmailAddress().toLowerCase().contains(checkValue));
            });
        });

/*
        readersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Book>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Book>> observableValue, TreeItem<Book> bookTreeItem, TreeItem<Book> t1) {
                changeUpdateBookForm();
            }
        });*/
    }

    @FXML
    public void clearAddReaderForm() {
       firstNameAddField.clear();
       lastNameAddField.clear();
       dateOfBirthAddField.getEditor().clear();
       phoneNumberAddField.clear();
       emailAddressAddField.clear();
    }

    @FXML
    public void clearUpdateReaderForm() {
        firstNameUpdateField.clear();
        lastNameUpdateField.clear();
        dateOfBirthUpdateField.getEditor().clear();
        phoneNumberUpdateField.clear();
        emailAddressUpdateField.clear();
    }
}
