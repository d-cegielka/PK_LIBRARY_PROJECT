package org.pk.library.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.FlowPane;
import org.pk.library.model.Book;
import org.pk.library.model.Library;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class BookController {
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
    JFXTreeTableColumn<Book,String> id;

    private static final String COMPUTER_DEPARTMENT = "Computer Department";
    private static final String SALES_DEPARTMENT = "Sales Department";
    private static final String IT_DEPARTMENT = "IT Department";
    private static final String HR_DEPARTMENT = "HR Department";

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
       isbnPattern = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
       //initializeBookTableViewColumn2();

    }

    @FXML
    public void initializeBookTableViewColumn2() {
        JFXTreeTableColumn<User, String> deptColumn = new JFXTreeTableColumn<>("Department");
        deptColumn.setPrefWidth(150);
        deptColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> param) ->{
            if(deptColumn.validateValue(param)) return param.getValue().getValue().department;
            else return deptColumn.getComputedValue(param);
        });

        JFXTreeTableColumn<User, String> empColumn = new JFXTreeTableColumn<>("Employee");
        empColumn.setPrefWidth(150);
        empColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> param) ->{
            if(empColumn.validateValue(param)) return param.getValue().getValue().userName;
            else return empColumn.getComputedValue(param);
        });

        JFXTreeTableColumn<User, String> ageColumn = new JFXTreeTableColumn<>("Age");
        ageColumn.setPrefWidth(150);
        ageColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> param) ->{
            if(ageColumn.validateValue(param)) return param.getValue().getValue().age;
            else return ageColumn.getComputedValue(param);
        });


        // data
        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(new User(COMPUTER_DEPARTMENT, "23", "CD 1"));
        users.add(new User(SALES_DEPARTMENT, "22", "Employee 1"));
        users.add(new User(SALES_DEPARTMENT, "24", "Employee 2"));
        users.add(new User(SALES_DEPARTMENT, "25", "Employee 4"));
        users.add(new User(SALES_DEPARTMENT, "27", "Employee 5"));
        users.add(new User(IT_DEPARTMENT, "42", "ID 2"));
        users.add(new User(HR_DEPARTMENT, "21", "HR 1"));
        users.add(new User(HR_DEPARTMENT, "28", "HR 2"));

        for (int i = 0; i < 40000; i++) {
            users.add(new User(HR_DEPARTMENT, Integer.toString(i % 10), "HR 3" + i));
        }
        for (int i = 0; i < 40000; i++) {
            users.add(new User(COMPUTER_DEPARTMENT, Integer.toString(i % 20), "CD 2" + i));
        }

        for (int i = 0; i < 40000; i++) {
            users.add(new User(IT_DEPARTMENT, Integer.toString(i % 5), "HR 4" + i));
        }

        // build tree
        final TreeItem<User> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);

        JFXTreeTableView<User> treeView = new JFXTreeTableView<>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(deptColumn, ageColumn, empColumn);

       /* FlowPane main = new FlowPane();
        main.setPadding(new Insets(10));
        main.getChildren().add(treeView);


        JFXButton groupButton = new JFXButton("Group");
        groupButton.setOnAction((action) -> new Thread(() -> treeView.group(empColumn)).start());
        main.getChildren().add(groupButton);

        JFXButton unGroupButton = new JFXButton("unGroup");
        unGroupButton.setOnAction((action) -> treeView.unGroup(empColumn));
        main.getChildren().add(unGroupButton);

        JFXTextField filterField = new JFXTextField();
        main.getChildren().add(filterField);

        Label size = new Label();

        filterField.textProperty().addListener((o, oldVal, newVal) -> {
            treeView.setPredicate(userProp -> {
                final User user = userProp.getValue();
                return user.age.get().contains(newVal)
                        || user.department.get().contains(newVal)
                        || user.userName.get().contains(newVal);
            });
        });

        size.textProperty()
                .bind(Bindings.createStringBinding(() -> String.valueOf(treeView.getCurrentItemsCount()),
                        treeView.currentItemsCountProperty()));
        main.getChildren().add(size);

        Scene scene = new Scene(main, 475, 500);
        scene.getStylesheets().add(TreeTableDemo.class.getResource("/css/jfoenix-components.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();*/

    }

    @FXML
    public void initializeBookTableViewColumn(){
        titleCol = new JFXTreeTableColumn<>("Tytuł");
        titleCol.setPrefWidth(150);
        titleCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(titleCol.validateValue(param)) return param.getValue().getValue().title;
            else return titleCol.getComputedValue(param);
        });

        isbnCol = new JFXTreeTableColumn<>("ISBN");
        isbnCol.setPrefWidth(150);
        isbnCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(isbnCol.validateValue(param)) return param.getValue().getValue().isbn;
            else return isbnCol.getComputedValue(param);
        });

        authorCol = new JFXTreeTableColumn<>("Autor");
        authorCol.setPrefWidth(150);
        authorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(authorCol.validateValue(param)) return param.getValue().getValue().author;
            else return authorCol.getComputedValue(param);
        });

        publisherCol = new JFXTreeTableColumn<>("Wydawnictwo");
        publisherCol.setPrefWidth(150);
        publisherCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(publisherCol.validateValue(param)) return param.getValue().getValue().publisher;
            else return publisherCol.getComputedValue(param);
        });

        titleCol.setCellFactory((TreeTableColumn<Book, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
     /*   ageColumn.setOnEditCommit((CellEditEvent<User, String> t) -> t.getTreeTableView()
                .getTreeItem(t.getTreeTablePosition()
                        .getRow())
                .getValue().age.set(t.getNewValue()));

        empColumn.setCellFactory((TreeTableColumn<User, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        empColumn.setOnEditCommit((CellEditEvent<User, String> t) -> t.getTreeTableView()
                .getTreeItem(t.getTreeTablePosition()
                        .getRow())
                .getValue().userName.set(t.getNewValue()));

        deptColumn.setCellFactory((TreeTableColumn<User, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        deptColumn.setOnEditCommit((CellEditEvent<User, String> t) -> t.getTreeTableView()
                .getTreeItem(t.getTreeTablePosition()
                        .getRow())
                .getValue().department.set(t.getNewValue()));*/

        id = new JFXTreeTableColumn<>("id");
        id.setPrefWidth(150);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->{
            if(id.validateValue(param)) return param.getValue().getValue().bookIDProperty();
            else return id.getComputedValue(param);
        });

        // data
        ObservableList<Book> users = FXCollections.observableArrayList();
        users.add(new Book("Computer Department", "23","CD 1","1"));
        users.add(new Book("Sales Department", "22","Employee 1","1"));

        //ObservableList<Book> books = mainController.library.getBooks();
        final TreeItem<Book> rootBook = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);

        //booksTableView = new JFXTreeTableView<>(rootBook, users);
        Platform.runLater(()->  {
            booksTableView = new JFXTreeTableView<>(rootBook);

        });

        booksTableView.setShowRoot(false);
        booksTableView.setEditable(false);
        booksTableView.getColumns().setAll(titleCol,isbnCol,authorCol,publisherCol,id);

        /*JFXButton groupButton = new JFXButton("Group");
        groupButton.setOnAction((action) -> new Thread(() -> booksTableView.group(titleCol)).start());
        main.getChildren().add(groupButton);

        JFXButton unGroupButton = new JFXButton("unGroup");
        unGroupButton.setOnAction((action) -> booksTableView.unGroup(titleCol));
        main.getChildren().add(unGroupButton);
*/
        findBookField.textProperty().addListener((o, oldVal, newVal) -> {
            booksTableView.setPredicate(userProp -> {
                final Book user = userProp.getValue();
                return user.getTitle().contains(newVal);
            });
        });

        listBookLabel.textProperty()
                .bind(Bindings.createStringBinding(() -> String.valueOf(booksTableView.getCurrentItemsCount()),
                        booksTableView.currentItemsCountProperty()));
      /*  main.getChildren().add(listBookLabel);*/

    }

    @FXML
    public void addBook(ActionEvent actionEvent) {
        try {
            if(bookIsbnAddField.getText().trim().isEmpty() || bookTitleAddField.getText().trim().isEmpty() ||
                    bookAuthorAddField.getText().trim().isEmpty() || bookPublisherAddField.getText().trim().isEmpty()) {
                throw new Exception("Uzupełnij wszystkie pola!");
            }
            if(!(isbnPattern.matcher(bookIsbnAddField.getText().trim())).matches())
            {
                //ISBN 978-0-596-52068-7 - valid
                //ISBN 11978-0-596-52068-7 - invalid
                throw new Exception("Niepoprawny numer ISBN!");
            }
         /*   if(!mainController.library.addBook(new Book(bookIsbnAddField.getText().trim(), bookTitleAddField.getText().trim(),
                    bookAuthorAddField.getText().trim(), bookPublisherAddField.getText().trim()))){
                throw new Exception("Nie udało się dodać książki!");
            }*/

            mainController.showInfoDialog("Informacja", "Książka została dodana pomyślnie!");
        } catch (Exception e) {
            mainController.showInfoDialog("Sprawdzenie formularza",e.getMessage());
        }
    }

    @FXML
    public void clearAddBookForm(ActionEvent actionEvent) {
        bookTitleAddField.clear();
        bookIsbnAddField.clear();
        bookAuthorAddField.clear();
        bookPublisherAddField.clear();
    }

    @FXML
    public void updateBook(ActionEvent actionEvent) {
        booksTableView.getSelectionModel().getSelectedItem().getValue().getPublisher();

    }

    @FXML
    public void clearUpdateBookForm(ActionEvent actionEvent) {
        bookTitleUpdateField.clear();
        bookIsbnUpdateField.clear();
        bookAuthorUpdateField.clear();
        bookPublisherUpdateField.clear();
    }
 /*   public void addReader(String name, String lastName, LocalDate dateOfBirth, String phoneNumber, String emailAddress) throws Exception {
        if(name.isEmpty() || lastName.isEmpty() || dateOfBirth.toString().isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty()) {
            throw new Exception("Uzupełnij wszystkie pola!");
        }
        //library.addBook();
    }*/

    public static final class Book extends RecursiveTreeObject<Book> {
        final StringProperty isbn;
        final StringProperty title;
        final StringProperty author;
        final StringProperty publisher;
        final StringProperty bookID;

        public Book(String isbn, String title, String author, String publisher) {
            this.isbn = new SimpleStringProperty(isbn);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.bookID = new SimpleStringProperty(UUID.randomUUID().toString());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Book{");
            sb.append("isbn='").append(isbn).append('\'');
            sb.append(", title='").append(title).append('\'');
            sb.append(", author='").append(author).append('\'');
            sb.append(", publisher='").append(publisher).append('\'');
            sb.append(", bookID='").append(bookID).append('\'');
            sb.append('}');
            return sb.toString();
        }


        @Override
        public int hashCode() {
            return Objects.hash(isbn, title, author, publisher);
        }


   /* public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBookID() {
        return bookID;
    }*/

        public String getIsbn() {
            return isbn.get();
        }

        public StringProperty isbnProperty() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn.set(isbn);
        }

        public String getTitle() {
            return title.get();
        }

        public StringProperty titleProperty() {
            return title;
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public String getAuthor() {
            return author.get();
        }

        public StringProperty authorProperty() {
            return author;
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public String getPublisher() {
            return publisher.get();
        }

        public StringProperty publisherProperty() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher.set(publisher);
        }

        public String getBookID() {
            return bookID.get();
        }

        public StringProperty bookIDProperty() {
            return bookID;
        }

        public void setBookID(String bookID) {
            this.bookID.set(bookID);
        }
    }
    private static final class User extends RecursiveTreeObject<User> {
        final StringProperty userName;
        final StringProperty age;
        final StringProperty department;

        User(String department, String age, String userName) {
            this.department = new SimpleStringProperty(department);
            this.userName = new SimpleStringProperty(userName);
            this.age = new SimpleStringProperty(age);
        }
    }

}


