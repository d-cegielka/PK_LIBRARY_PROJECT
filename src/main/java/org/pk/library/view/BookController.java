package org.pk.library.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.pk.library.model.Book;

import java.util.Optional;

public class BookController {
    @FXML
    private MainController mainController;
    @FXML
    private JFXTextField findBookField;
    @FXML
    private JFXTextField bookTitleAddField;
    @FXML
    private JFXTextField bookIsbnAddField;
    @FXML
    private JFXTextField bookAuthorAddField;
    @FXML
    private JFXTextField bookPublisherAddField;
    @FXML
    private JFXTextField bookTitleUpdateField;
    @FXML
    private JFXTextField bookIsbnUpdateField;
    @FXML
    private JFXTextField bookAuthorUpdateField;
    @FXML
    public JFXTextField bookPublisherUpdateField;
    @FXML
    private JFXTreeTableView<Book> booksTableView;
    @FXML
    private JFXTreeTableColumn<Book, String> titleCol;
    @FXML
    private JFXTreeTableColumn<Book, String> isbnCol;
    @FXML
    private JFXTreeTableColumn<Book, String> publisherCol;
    @FXML
    private JFXTreeTableColumn<Book, String> authorCol;
    @FXML
    private JFXButton deleteBookButton;

    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeBookTableView();
    }

    /**
     * Inicializacja kolumn w tabeli z listą książek oraz wyszukiwarka książek
     */
    void initializeBookTableView(){
        titleCol = new JFXTreeTableColumn<>("Tytuł");
        titleCol.prefWidthProperty().bind(booksTableView.widthProperty().subtract(16).divide(4));
        titleCol.setResizable(false);
        titleCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(titleCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTitle());
            else return titleCol.getComputedValue(param);
        });

        isbnCol = new JFXTreeTableColumn<>("ISBN");
        isbnCol.prefWidthProperty().bind(booksTableView.widthProperty().subtract(16).divide(4));
        isbnCol.setResizable(false);
        isbnCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(isbnCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getIsbn());
            else return isbnCol.getComputedValue(param);
        });

        authorCol = new JFXTreeTableColumn<>("Autor");
        authorCol.prefWidthProperty().bind(booksTableView.widthProperty().subtract(16).divide(4));
        authorCol.setResizable(false);
        authorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(authorCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getAuthor());
            else return authorCol.getComputedValue(param);
        });

        publisherCol = new JFXTreeTableColumn<>("Wydawnictwo");
        publisherCol.prefWidthProperty().bind(booksTableView.widthProperty().subtract(16).divide(4));
        publisherCol.setResizable(false);
        publisherCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(publisherCol.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getPublisher());
            else return publisherCol.getComputedValue(param);
        });

        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getBooks()), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);

        booksTableView.setShowRoot(false);
        booksTableView.setEditable(true);
        booksTableView.getColumns().setAll(titleCol,isbnCol,authorCol,publisherCol);

        findBookField.textProperty().addListener((o, oldVal, newVal) -> booksTableView.setPredicate(bookProp -> {
            final Book book = bookProp.getValue();
            String checkValue = newVal.trim().toLowerCase();
            return book.toString().toLowerCase().contains(checkValue);
        }));

        booksTableView.currentItemsCountProperty().addListener((observableValue, rentTreeItem, t1) -> {
                    if (booksTableView.getCurrentItemsCount() == 0) clearUpdateBookForm();
        });
        booksTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, rentTreeItem, t1) -> changeUpdateBookForm());
}

    /**
     * Aktualizacja listy książęk
     */
    private void reloadBookTableView(){
        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getBooks()), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);

    }

    /**
     * Dodawanie książki do struktury danych
     */
    @FXML
    private void addBook() {
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.addBook(
                        bookIsbnAddField.getText(),
                        bookTitleAddField.getText(),
                        bookAuthorAddField.getText(),
                        bookPublisherAddField.getText()
                )
        );
        reloadBookTableView();
    }

    /**
     * Aktualizacja książki w strukturze danych
     */
    @FXML
    private void updateBook() {
        int bookIndex = booksTableView.getSelectionModel().getSelectedIndex();
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.updateBook(
                        booksTableView.getSelectionModel().getSelectedItem().getValue(),
                        bookIsbnUpdateField.getText(),
                        bookTitleUpdateField.getText(),
                        bookAuthorUpdateField.getText(),
                        bookPublisherUpdateField.getText()
                )
        );
        reloadBookTableView();
        booksTableView.getSelectionModel().select(bookIndex);
    }

    /**
     * Usuwanie książki z struktury danych
     */
    @FXML
    private void deleteBook() {
        final Book bookToRemove = booksTableView.getSelectionModel().getSelectedItem().getValue();
        if(confirmDeletionBook(bookToRemove.getTitle())) {
            mainController.showInfoDialog(
                    "Informacja",
                    mainController.libraryController.deleteBook(bookToRemove)
            );
        }
        reloadBookTableView();
    }

    /**
     * Aktualizacja danych formularza po wybraniu ksiązki z listy
     */
    @FXML
    private void changeUpdateBookForm() {
        if(booksTableView.getSelectionModel().getSelectedItem() != null) {
            bookTitleUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getTitle());
            bookPublisherUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getPublisher());
            bookIsbnUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getIsbn());
            bookAuthorUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getAuthor());
            deleteBookButton.setDisable(false);
        }
    }

    /**
     * Metoda wypisująca inforamcję w oknie dialogowym podczas usuwania książki
     * @param title tytuł książki
     * @return potwierdzenie/niepotwierdzenie usunięcia
     */
    @FXML
    boolean confirmDeletionBook(String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdź wybór");
        alert.setHeaderText("Czy na pewno chcesz usunąć książkę pt. \"" + title + "\" ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;
    }

    /**
     * Czyszczenie pól formularza dodawania książki
     */
    @FXML
    private void clearAddBookForm() {
        bookTitleAddField.clear();
        bookIsbnAddField.clear();
        bookAuthorAddField.clear();
        bookPublisherAddField.clear();
    }

    /**
     * Czyszczenie pól formularza aktualizacji książki
     */
    @FXML
    private void clearUpdateBookForm() {
        bookTitleUpdateField.clear();
        bookIsbnUpdateField.clear();
        bookAuthorUpdateField.clear();
        bookPublisherUpdateField.clear();
        deleteBookButton.setDisable(true);
    }
}