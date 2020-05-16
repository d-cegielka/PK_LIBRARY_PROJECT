package org.pk.library.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.pk.library.model.Rent;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarController {
    @FXML
    private JFXTextField numOfDaysExtendRentalPeriod;
    @FXML
    private JFXButton extendRentalPeriodButton;
    @FXML
    private JFXButton cancelReturnBookButton;
    @FXML
    private StackPane calendarStackPane;
    @FXML
    private JFXButton returnBookButton;
    @FXML
    private MainController mainController;
    @FXML
    private CalendarView returnCalendarView;
    @FXML
    private Calendar rentsCalendar;
    @FXML
    private Calendar rentsCalendarReturned;


    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeCalendar();
        numOfDaysExtendRentalPeriod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numOfDaysExtendRentalPeriod.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    void initializeCalendar(){
        returnCalendarView = new CalendarView();
        rentsCalendar = new Calendar("Wypożyczenia aktualne");
        rentsCalendarReturned = new Calendar("Wypożyczenia archwialne");
        CalendarSource rentsSource = new CalendarSource("Wypożyczenia");
        rentsSource.getCalendars().addAll(rentsCalendar,rentsCalendarReturned);
        returnCalendarView.setRequestedTime(LocalTime.now());
        rentsCalendar.setStyle(Calendar.Style.STYLE1);
        rentsCalendarReturned.setStyle(Calendar.Style.STYLE2);
        returnCalendarView.showMonthPage();
        calendarStackPane.getChildren().clear();
        calendarStackPane.getChildren().add(returnCalendarView);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        returnCalendarView.setToday(LocalDate.now());
                        returnCalendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        returnCalendarView.getCalendarSources().clear();
        returnCalendarView.getCalendarSources().add(rentsSource);
        rentsCalendar.setReadOnly(true);
        rentsCalendarReturned.setReadOnly(true);

        loadRentsCalendar();
      /*  for (Rent rent : mainController.libraryController.getRents()) {
            addRentToCalendar(rent);
        }*/

        returnCalendarView.getSelections().addListener((InvalidationListener) changes -> {
            ObservableList<Entry<?>> selectedRent = FXCollections.observableArrayList(returnCalendarView.getSelections());
            if (selectedRent.size() == 1) {
                if(rentsCalendar.findEntries("").contains(selectedRent.get(0))){
                    returnBookButton.setDisable(false);
                    cancelReturnBookButton.setDisable(true);
                    extendRentalPeriodButton.setDisable(false);
                } else {
                    returnBookButton.setDisable(true);
                    cancelReturnBookButton.setDisable(false);
                }
            } else {
                returnBookButton.setDisable(true);
                cancelReturnBookButton.setDisable(true);
                extendRentalPeriodButton.setDisable(true);
            }

        });

    }

    void loadRentsCalendar() {
        rentsCalendarReturned.clear();
        rentsCalendar.clear();
        for (Rent rent : mainController.libraryController.getRents()) {
            Entry<Rent> addRent = new Entry<>("Książka: " + rent.getBOOK().getTitle() +
                    " \nCzytelnik: " + rent.getREADER().getFirstName() + " " + rent.getREADER().getLastName());
            addRent.setInterval(rent.getDateOfReturn());
            addRent.setUserObject(rent);
            if(rent.isReturned()) {
                rentsCalendarReturned.addEntry(addRent);
            } else {
                rentsCalendar.addEntry(addRent);
            }
        }
    }

    @FXML
    private void returnBook(){
        ObservableList<Entry<?>> returnRentList = FXCollections.observableArrayList(returnCalendarView.getSelections());
        Rent returnRent = (Rent) returnRentList.get(0).getUserObject();
        String message = mainController.libraryController.returnBook(returnRent,true);
        mainController.showInfoDialog("Informacja", message);
        if(message.contains("zwrócona pomyślnie")) {
            rentsCalendar.removeEntry(returnRentList.get(0));
            rentsCalendarReturned.addEntry(returnRentList.get(0));
            mainController.reloadRentView();
            returnBookButton.setDisable(true);
            cancelReturnBookButton.setDisable(false);
        }
    }

    @FXML
    private void cancelReturnBook(){
        ObservableList<Entry<?>> cancelReturnRentList = FXCollections.observableArrayList(returnCalendarView.getSelections());
        Rent returnRent = (Rent) cancelReturnRentList.get(0).getUserObject();
        String message = mainController.libraryController.returnBook(returnRent,false);
        mainController.showInfoDialog("Informacja", message);
        if(message.contains("wycofany pomyślnie")) {
            rentsCalendarReturned.removeEntry(cancelReturnRentList.get(0));
            rentsCalendar.addEntry(cancelReturnRentList.get(0));
            mainController.reloadRentView();
            returnBookButton.setDisable(false);
            cancelReturnBookButton.setDisable(true);
        }
    }

    @FXML
    private void extendRentalPeriod() {
        ObservableList<Entry<?>> returnRentList = FXCollections.observableArrayList(returnCalendarView.getSelections());
        Rent rent = (Rent) returnRentList.get(0).getUserObject();
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.extendRentalPeriod(
                        rent,
                        numOfDaysExtendRentalPeriod.getText()
                )
        );
        returnRentList.get(0).setInterval(rent.getDateOfReturn());
        mainController.reloadRentView();
    }

}
