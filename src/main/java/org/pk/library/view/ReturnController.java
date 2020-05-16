package org.pk.library.view;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import org.pk.library.model.Book;
import org.pk.library.model.Reader;
import org.pk.library.model.Rent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ReturnController {
    private MainController mainController;
    private RentController rentController;
    @FXML
    private JFXTreeTableView<Rent> returnTableView;
    @FXML
    private JFXTreeTableColumn<Rent, Book> bookCol;
    @FXML
    private JFXTreeTableColumn<Rent, Reader> readerCol;
    @FXML
    private JFXTreeTableColumn<Rent, LocalDateTime> dateOfRentCol;
    @FXML
    private JFXTreeTableColumn<Rent, LocalDateTime> dateOfReturnCol;
    @FXML
    private JFXTreeTableColumn<Rent, Boolean> returnedCol;
    @FXML
    private JFXDatePicker newReturnDateDataPicker;
    /*@FXML
    private CalendarView returnCalendar;

    @FXML
    void initializeCalendar(){
        System.out.println(returnCalendar.getCalendarSources().size());
        //com.calendarfx.model.Calendar rentsCalendar = new com.calendarfx.model.Calendar("Wypożyczenia");
        CalendarSource rentsSource = new CalendarSource("Wypożyczenia");
        //rentsSource.getCalendars().addAll(rentsCalendar);
        //returnCalendar.getCalendarSources().add(rentsSource);
    }*/

    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeReturnTableView();
    }

    void initializeReturnTableView() {
        bookCol = new JFXTreeTableColumn<>("Książka");
        bookCol.prefWidthProperty().bind(returnTableView.widthProperty().divide(5));
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
        readerCol.prefWidthProperty().bind(returnTableView.widthProperty().divide(5));
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
        dateOfRentCol.prefWidthProperty().bind(returnTableView.widthProperty().divide(5));
        dateOfRentCol.setResizable(false);
        dateOfRentCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, LocalDateTime> param) ->{
            if(dateOfRentCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfRent());
            else return dateOfRentCol.getComputedValue(param);
        });

        dateOfReturnCol = new JFXTreeTableColumn<>("Data zwrotu");
        dateOfReturnCol.prefWidthProperty().bind(returnTableView.widthProperty().divide(5));
        dateOfReturnCol.setResizable(false);
        dateOfReturnCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rent, LocalDateTime> param) ->{
            if(dateOfReturnCol.validateValue(param)) return new SimpleObjectProperty<>(param.getValue().getValue().getDateOfReturn());
            else return dateOfReturnCol.getComputedValue(param);
        });

        returnedCol = new JFXTreeTableColumn<>("Czy zwrócono");
        returnedCol.prefWidthProperty().bind(returnTableView.widthProperty().divide(5));
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
        returnTableView.setRoot(rentsTreeItem);

        returnTableView.setShowRoot(false);
        returnTableView.setEditable(true);
        returnTableView.getColumns().setAll(bookCol,readerCol,dateOfRentCol,dateOfReturnCol,returnedCol);

        /*returnTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            changeDataPicker();
        });*/
    }


    @FXML
    void reloadReturnTableView(){
        TreeItem<Rent> rentsTreeItem = new RecursiveTreeItem<>(FXCollections.observableArrayList(mainController.libraryController.getRents()), RecursiveTreeObject::getChildren);
        returnTableView.setRoot(rentsTreeItem);
    }

    /*@FXML
    private void extendRentalPeriod() {
        int rentIndex = returnTableView.getSelectionModel().getSelectedIndex();
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.extendRentalPeriod(
                        returnTableView.getSelectionModel().getSelectedItem().getValue(),
                        newReturnDateDataPicker.getValue()
                )
        );
        reloadReturnTableView();
        returnTableView.getSelectionModel().select(rentIndex);
    }

    @FXML
    private void changeDataPicker() {
        if(!returnTableView.getSelectionModel().isEmpty()) {
            newReturnDateDataPicker.setValue(returnTableView.getSelectionModel().getSelectedItem().getValue().getDateOfReturn());
        }
    }

    @FXML
    private void initializeCalendar() {

        Calendar calendar = new Calendar();

    }*/

}
