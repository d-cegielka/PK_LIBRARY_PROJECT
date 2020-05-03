package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.pk.library.model.Book;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class BookController {
    public JFXButton addBookButton;
    private MainController mainController;
    public JFXTextField findBookField;
    public JFXTextField bookTitleAddField;
    public JFXTextField bookIsbnAddField;
    public JFXTextField bookAuthorAddField;
    public JFXTextField bookPublisherAddField;
    public JFXTextField bookTitleUpdateField;
    public JFXTextField bookIsbnUpdateField;
    public JFXTextField bookAuthorUpdateField;
    public JFXTextField bookPublisherUpdateField;
    public Label listBookLabel;
    Pattern isbnPattern;

    @FXML
    JFXTreeTableView<Book> booksTableView;
    JFXTreeTableColumn<Book,String> titleCol;
    JFXTreeTableColumn<Book,String> isbnCol;
    JFXTreeTableColumn<Book,String> publisherCol;
    JFXTreeTableColumn<Book,String> authorCol;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
       isbnPattern = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }

    public void initializeBookTableView(){

        titleCol = new JFXTreeTableColumn<>("Tytuł");
        titleCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(4));
        titleCol.setResizable(false);
        titleCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(titleCol.validateValue(param)) return param.getValue().getValue().titleProperty();
            else return titleCol.getComputedValue(param);
        });

        isbnCol = new JFXTreeTableColumn<>("ISBN");
        isbnCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(4));
        isbnCol.setResizable(false);
        isbnCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(isbnCol.validateValue(param)) return param.getValue().getValue().isbnProperty();
            else return isbnCol.getComputedValue(param);
        });

        authorCol = new JFXTreeTableColumn<>("Autor");
        authorCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(4));
        authorCol.setResizable(false);
        authorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(authorCol.validateValue(param)) return param.getValue().getValue().authorProperty();
            else return authorCol.getComputedValue(param);
        });

        publisherCol = new JFXTreeTableColumn<>("Wydawnictwo");
        publisherCol.prefWidthProperty().bind(booksTableView.widthProperty().divide(4));
        publisherCol.setResizable(false);
        publisherCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(publisherCol.validateValue(param)) return param.getValue().getValue().publisherProperty();
            else return publisherCol.getComputedValue(param);
        });

        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(mainController.library.getBooks(), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);

        booksTableView.setShowRoot(false);
        booksTableView.setEditable(true);
        booksTableView.getColumns().setAll(titleCol,isbnCol,authorCol,publisherCol);

        findBookField.textProperty().addListener((o, oldVal, newVal) -> {
            booksTableView.setPredicate(bookProp -> {
                final Book book = bookProp.getValue();
                String checkValue = newVal.trim().toLowerCase();
                return (book.getTitle().toLowerCase().contains(checkValue) ||
                        book.getPublisher().toLowerCase().contains(checkValue) ||
                        book.getAuthor().toLowerCase().contains(checkValue) ||
                        book.getIsbn().toLowerCase().contains(checkValue) ||
                        book.getBookID().toLowerCase().contains(checkValue));
            });
        });


        booksTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Book>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Book>> observableValue, TreeItem<Book> bookTreeItem, TreeItem<Book> t1) {
                changeUpdateBookForm();
            }
        });
}

    /**
     * Aktualizacja listy książęk
     */
    private void reloadBookTableView(){
        TreeItem<Book> booksTreeItem = new RecursiveTreeItem<>(mainController.library.getBooks(), RecursiveTreeObject::getChildren);
        booksTableView.setRoot(booksTreeItem);
    }

    /**
     * Dodawanie książki do struktury danych
     */
    @FXML
    private void addBook() {
        Book bookToAdd = null;
        try {
            if(bookIsbnAddField.getText().trim().isEmpty() || bookTitleAddField.getText().trim().isEmpty() ||
                    bookAuthorAddField.getText().trim().isEmpty() || bookPublisherAddField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Uzupełnij wszystkie pola!");
            }
            if(!(isbnPattern.matcher(bookIsbnAddField.getText().trim())).matches()) {
                //ISBN 978-0-596-52068-7 - valid
                //ISBN 11978-0-596-52068-7 - invalid
                throw new IllegalArgumentException("Niepoprawny numer ISBN!");
            }
            bookToAdd = new Book(bookIsbnAddField.getText().trim(), bookTitleAddField.getText().trim(),
                    bookAuthorAddField.getText().trim(), bookPublisherAddField.getText().trim());
            if(!mainController.library.addBook(bookToAdd))  {
                throw new Exception("Nie udało się dodać książki!");
            }
            if(mainController.libraryDB != null) {
                mainController.libraryDB.insertBook(bookToAdd);
            }

            mainController.showInfoDialog("Informacja", "Książka została dodana pomyślnie!");
        } catch (SQLException se){
            mainController.library.removeBook(bookToAdd);
            mainController.showInfoDialog("Informacja SQL",se.getMessage());
        } catch (IllegalArgumentException iae) {
            mainController.showInfoDialog("Sprawdzenie formularza",iae.getMessage());
        } catch (Exception e) {
            mainController.showInfoDialog("Informacja",e.getMessage());
        }
        reloadBookTableView();
    }

    /**
     * Aktualizacja książki w strukturze danych
     */
    @FXML
    private void updateBook() {
        try {
            int changesNum = 0;
            if (bookIsbnUpdateField.getText().trim().isEmpty() || bookTitleUpdateField.getText().trim().isEmpty() ||
                    bookAuthorUpdateField.getText().trim().isEmpty() || bookPublisherUpdateField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Uzupełnij wszystkie pola!");
            }
            if (!bookTitleUpdateField.getText().trim().equals(booksTableView.getSelectionModel().getSelectedItem().getValue().getTitle())) {
                booksTableView.getSelectionModel().getSelectedItem().getValue().setTitle(bookTitleUpdateField.getText());
                changesNum++;
            }
            if (!bookIsbnUpdateField.getText().trim().equals(booksTableView.getSelectionModel().getSelectedItem().getValue().getIsbn())) {
                if (!(isbnPattern.matcher(bookIsbnUpdateField.getText().trim())).matches()) {
                    //ISBN 978-0-596-52068-7 - valid
                    //ISBN 11978-0-596-52068-7 - invalid
                    throw new IllegalArgumentException("Niepoprawny numer ISBN!");
                }
                booksTableView.getSelectionModel().getSelectedItem().getValue().setIsbn(bookIsbnUpdateField.getText().trim());
                changesNum++;
            }

            if (!bookAuthorUpdateField.getText().trim().equals(booksTableView.getSelectionModel().getSelectedItem().getValue().getAuthor())) {
                booksTableView.getSelectionModel().getSelectedItem().getValue().setAuthor(bookAuthorUpdateField.getText().trim());
                changesNum++;
            }
            if (!bookPublisherUpdateField.getText().trim().equals(booksTableView.getSelectionModel().getSelectedItem().getValue().getPublisher())) {
                booksTableView.getSelectionModel().getSelectedItem().getValue().setPublisher(bookPublisherUpdateField.getText().trim());
                changesNum++;
            }

            if(changesNum > 0) {
                mainController.libraryDB.updateBook(booksTableView.getSelectionModel().getSelectedItem().getValue());
                mainController.showInfoDialog("Informacja", "Książka została zaktualizowana pomyślnie!\n" +
                        "Ilość wprowadzonych zmian: " + changesNum);
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
     * Usuwanie książki z struktury danych
     */
    @FXML
    private void deleteBook() {
        final Book bookToRemove = booksTableView.getSelectionModel().getSelectedItem().getValue();
        try {
            if(mainController.libraryDB.deleteFromTable("BOOKS",bookToRemove.getBookID()) && mainController.library.removeBook(bookToRemove)){
                mainController.showInfoDialog("Informacja", "Książka została usunięta pomyślnie!");
            }
            else {
                mainController.showInfoDialog("Informacja", "Książka nie została usunięta!");
            }
        } catch (SQLException se) {
            mainController.showInfoDialog("Informacja SQL",se.getMessage());
        }
        reloadBookTableView();
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
     * Aktualizacja danych formularza po wybraniu ksiązki z listy
     */
    @FXML
    private void changeUpdateBookForm() {
        clearUpdateBookForm();
        if(!booksTableView.getSelectionModel().isEmpty() && booksTableView.isHover() && !findBookField.isFocused()) {
            bookTitleUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getTitle());
            bookPublisherUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getPublisher());
            bookIsbnUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getIsbn());
            bookAuthorUpdateField.setText(booksTableView.getSelectionModel().getSelectedItem().getValue().getAuthor());
        }
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
    }

    @FXML
    private void updateBookTestData(){
        for(int i=0; i< 10000;i++)
        {
            Book book1;
            book1 = new Book("5869782"+i,"Harry Potter", "J.K Rowling","Bloomsbury Publishing");
            mainController.library.addBook(book1);
        }
        reloadBookTableView();
    }

}


