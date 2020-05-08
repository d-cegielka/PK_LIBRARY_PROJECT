package org.pk.library.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import org.pk.library.model.Book;
import org.pk.library.model.Reader;
import org.pk.library.model.Rent;

import java.time.LocalDate;

public class RentController {
    private MainController mainController;
    @FXML
    private JFXTextField findReaderField;
    @FXML
    private JFXTextField findBookField;
    @FXML
    private JFXDatePicker dateOfRentField;
    @FXML
    private JFXDatePicker dateOfReturnField;
    @FXML
    private JFXTextField findRentField;
    @FXML
    private JFXTreeTableView<Rent> rentsTableView;
    @FXML
    private JFXTreeTableColumn<Rent, Book> bookCol;
    @FXML
    private JFXTreeTableColumn<Rent, Reader> readerCol;
    @FXML
    private JFXTreeTableColumn<Rent, LocalDate> dateOfRentCol;
    @FXML
    private JFXTreeTableColumn<Rent, LocalDate> dateOfReturnCol;
    @FXML
    private JFXTreeTableColumn<Rent, Boolean> returnedCol;
    @FXML
    private JFXTreeTableView<Reader> readersTableView;
    @FXML
    private JFXTreeTableColumn<Reader, String> firstNameCol;
    @FXML
    private JFXTreeTableColumn<Reader, String> lastNameCol;
    @FXML
    private JFXTreeTableColumn<Reader, LocalDate> dateOfBirthCol;
    @FXML
    private JFXTreeTableView<Book> booksTableView;
    @FXML
    private JFXTreeTableColumn<Book, String> titleCol;
    @FXML
    private JFXTreeTableColumn<Book, String> authorCol;

    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeRentTableView();
        initializeBookTableViewInRent();
        initializeReaderTableViewInRent();
    }

    /**
     * Inicjalizacja kolumn w tabeli z listą wypożyczeń oraz wyszukiwarki wypożyczeń
     * Wyszukiwanie odbywa się również po wszystkich dostępnych danych książek i czytelników
     */
    void initializeRentTableView() {
        bookCol = new JFXTreeTableColumn<>("Książka");
        bookCol.prefWidthProperty().bind(rentsTableView.widthProperty().divide(5));
        bookCol.setResizable(false);
        bookCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, Book> param) -> {
            if(bookCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getBOOK());
            else return bookCol.getComputedValue(param);
        });
        bookCol.setCellFactory(column -> new TreeTableCell<>(){
            @Override
            protected void updateItem(Book book, boolean b) {
                if (book == getItem()) {
                    return;
                }
                super.updateItem(book, b);
                if (book == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else {
                    super.setText(book.getTitle());
                    super.setGraphic(null);
                }
            }
        });

        readerCol = new JFXTreeTableColumn<>("Czytelnik");
        readerCol.prefWidthProperty().bind(rentsTableView.widthProperty().divide(5));
        readerCol.setResizable(false);
        readerCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, Reader> param)->{
            if(readerCol.validateValue(param))
                return new SimpleObjectProperty<>(param.getValue().getValue().getREADER());
            else return readerCol.getComputedValue(param);
        });
        readerCol.setCellFactory(column -> new TreeTableCell<>(){
            @Override
            protected void updateItem(Reader reader, boolean b) {
                if (reader == getItem()) {
                    return;
                }
                super.updateItem(reader, b);
                if (reader == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else {
                    super.setText(reader.getFirstName() + " " + reader.getLastName());
                    super.setGraphic(null);
                }
            }
        } );

        dateOfRentCol = new JFXTreeTableColumn<>("Data wypożyczenia");
        dateOfRentCol.prefWidthProperty().bind(rentsTableView.widthProperty().divide(5));
        dateOfRentCol.setResizable(false);
        dateOfRentCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, LocalDate> param) ->{
            if(dateOfRentCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfRent());
            else return dateOfRentCol.getComputedValue(param);
        });

        dateOfReturnCol = new JFXTreeTableColumn<>("Data zwrotu");
        dateOfReturnCol.prefWidthProperty().bind(rentsTableView.widthProperty().divide(5));
        dateOfReturnCol.setResizable(false);
        dateOfReturnCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, LocalDate> param) ->{
            if(dateOfReturnCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfReturn());
            else return dateOfReturnCol.getComputedValue(param);
        });

        returnedCol = new JFXTreeTableColumn<>("Czy zwrócono");
        returnedCol.prefWidthProperty().bind(rentsTableView.widthProperty().divide(5));
        returnedCol.setResizable(false);
        returnedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, Boolean> param) ->{
            if(returnedCol.validateValue(param)) return new SimpleBooleanProperty(param.getValue().getValue().isReturned());
            else return returnedCol.getComputedValue(param);
        });

        returnedCol.setCellFactory(column -> new TreeTableCell<>(){
            @Override
            protected void updateItem(Boolean aBoolean, boolean b) {
                if (aBoolean == getItem()) {
                    return;
                }
                super.updateItem(aBoolean, b);
                if (aBoolean == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else {
                    if(aBoolean){
                        super.setText("tak");
                    } else{
                        super.setText("nie");
                    }
                    super.setGraphic(null);
                }
            }
        });

        TreeItem<Rent> rentsTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getRents()), RecursiveTreeObject::getChildren);
        rentsTableView.setRoot(rentsTreeItem);

        rentsTableView.setShowRoot(false);
        rentsTableView.setEditable(true);
        rentsTableView.getColumns().setAll(bookCol,readerCol,dateOfRentCol,dateOfReturnCol,returnedCol);

        findRentField.textProperty().addListener((o, oldVal, newVal) -> rentsTableView.setPredicate(readerProp -> {

            final Rent rent = readerProp.getValue();
            String checkValue = newVal.trim().toLowerCase();
            return (rent.getBOOK().toString().toLowerCase().contains(checkValue) ||
                    rent.getREADER().toString().toLowerCase().contains(checkValue) ||
                    rent.getDateOfRent().toString().contains(checkValue) ||
                    rent.getDateOfReturn().toString().contains(checkValue) ||
                    String.valueOf(rent.isReturned()).contains(checkValue));
        }));
    }

    /**
     * Inicjalizacja kolumn w tabeli z listą czytelników oraz wyszukiwarki czytelników w wypożyczaniu książki
     */
    void initializeReaderTableViewInRent(){
        firstNameCol = new JFXTreeTableColumn<>("Imię");
        firstNameCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(3));
        firstNameCol.setResizable(false);
        firstNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(firstNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getFirstName());
            else return firstNameCol.getComputedValue(param);
        });

        lastNameCol = new JFXTreeTableColumn<>("Nazwisko");
        lastNameCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(3));
        lastNameCol.setResizable(false);
        lastNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, String> param) ->{
            if(lastNameCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getLastName());
            else return lastNameCol.getComputedValue(param);
        });

        dateOfBirthCol = new JFXTreeTableColumn<>("Data urodzenia");
        dateOfBirthCol.prefWidthProperty().bind(readersTableView.widthProperty().divide(3));
        dateOfBirthCol.setResizable(false);
        dateOfBirthCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reader, LocalDate> param) ->{
            if(dateOfBirthCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfBirth());
            else return dateOfBirthCol.getComputedValue(param);
        });

        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getReaders()), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);

        readersTableView.setShowRoot(false);
        readersTableView.setEditable(true);
        readersTableView.getColumns().setAll(firstNameCol, lastNameCol, dateOfBirthCol);

        findReaderField.textProperty().addListener((o, oldVal, newVal) -> readersTableView.setPredicate(readerProp -> {
            final Reader reader = readerProp.getValue();
            String checkValue = newVal.trim().toLowerCase();
            return (reader.getFirstName().toLowerCase().contains(checkValue) ||
                    reader.getLastName().toLowerCase().contains(checkValue) ||
                    reader.getDateOfBirth().toString().contains(checkValue) ||
                    reader.getPhoneNumber().toLowerCase().contains(checkValue) ||
                    reader.getEmailAddress().toLowerCase().contains(checkValue));
        }));
    }

    /**
     * Inicjalizacja kolumn w tabeli z listą książek oraz wyszukiwarki książek w wypożyczaniu książki
     */
    void initializeBookTableViewInRent(){
        titleCol = new JFXTreeTableColumn<>("Tytuł");
        titleCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(2));
        titleCol.setResizable(false);
        titleCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(titleCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTitle());
            else return titleCol.getComputedValue(param);
        });

        authorCol = new JFXTreeTableColumn<>("Autor");
        authorCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(2));
        authorCol.setResizable(false);
        authorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(authorCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getAuthor());
            else return authorCol.getComputedValue(param);
        });


        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getBooks()), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);

        booksTableView.setShowRoot(false);
        booksTableView.setEditable(true);
        booksTableView.getColumns().setAll(titleCol,authorCol);

        findBookField.textProperty().addListener((o, oldVal, newVal) -> booksTableView.setPredicate(bookProp -> {
            final Book book = bookProp.getValue();
            String checkValue = newVal.trim().toLowerCase();
            return (book.getTitle().toLowerCase().contains(checkValue) ||
                    book.getPublisher().toLowerCase().contains(checkValue) ||
                    book.getAuthor().toLowerCase().contains(checkValue) ||
                    book.getIsbn().toLowerCase().contains(checkValue) ||
                    book.getBOOK_ID().toLowerCase().contains(checkValue));
        }));

       // booksTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> changeUpdateBookForm());

    }

    /**
     * Aktualizacja listy wypożyczeń
     */
    @FXML
    void reloadRentTableView(){
        TreeItem<Rent> rentsTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getRents()), RecursiveTreeObject::getChildren);
        rentsTableView.setRoot(rentsTreeItem);
    }

    /**
     * Aktualizacja listy czytelników
     */
    @FXML
    void reloadReadersTableView() {
        TreeItem<Reader> readersTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getReaders()), RecursiveTreeObject::getChildren);
        readersTableView.setRoot(readersTreeItem);
        readersTableView.getSelectionModel().select(0);
    }

    /**
     * Aktualizacja listy książek
     */
    @FXML
    void reloadBooksTableView(){
        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getBooks()), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);
        booksTableView.getSelectionModel().select(0);
    }

    /**
     * Dodawnie
     */
    @FXML
    private void addRent() {
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.addRent(
                        booksTableView.getSelectionModel().getSelectedItem().getValue(),
                        readersTableView.getSelectionModel().getSelectedItem().getValue(),
                        dateOfRentField.getValue(),
                        dateOfReturnField.getValue()
                )
        );
        reloadRentTableView();

    }



}
