package org.pk.library.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.pk.library.model.Rent;
import org.pk.library.model.RentalReminder;

import java.time.*;
import java.util.List;
import java.util.Map;

public class ReturnController {
    @FXML
    private JFXTimePicker timeOfReminderField;
    @FXML
    private JFXDatePicker dateOfReminderField;
    @FXML
    private JFXButton deleteReminderButton;
    @FXML
    private JFXButton addReminderButton;
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
    CalendarView returnCalendarView;
    @FXML
    private Calendar rentsCalendar;
    @FXML
    private Calendar rentsCalendarReturned;
    @FXML
    private Calendar rentalReminders;


    void injectMainController(MainController mainController) {
        this.mainController = mainController;
        initializeCalendars();
        numOfDaysExtendRentalPeriod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numOfDaysExtendRentalPeriod.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        timeOfReminderField.set24HourView(true);

    }

    /**
     * Inicjalizacja kalendarza.
     * @param name nazwa kalendarza
     * @param style styl kalendarza
     * @return zainicjalizowany kalendarz
     */
    @FXML
    private Calendar initializeCalendar(String name, Calendar.Style style){
        Calendar calendar = new Calendar(name);
        calendar.setStyle(style);
        calendar.setReadOnly(true);
        return calendar;
    }

    /**
     * Inicjalizacja kalendarzy z wypożyczeniami i przypomnieniami.
     */
    @FXML
    void initializeCalendars(){
        returnCalendarView = new CalendarView();
        rentsCalendar = initializeCalendar("Wypożyczenia aktualne",Calendar.Style.STYLE1);
        rentsCalendarReturned = initializeCalendar("Wypożyczenia archwialne",Calendar.Style.STYLE2);
        rentalReminders = initializeCalendar("Przypomnienia", Calendar.Style.STYLE3);
        CalendarSource rentsSource = new CalendarSource("Wypożyczenia");
        rentsSource.getCalendars().addAll(rentsCalendar,rentsCalendarReturned,rentalReminders);
        returnCalendarView.setRequestedTime(LocalTime.now());
        returnCalendarView.showMonthPage();
        returnCalendarView.setShowAddCalendarButton(false);
        returnCalendarView.setShowPrintButton(false);
        calendarStackPane.getChildren().clear();
        calendarStackPane.getChildren().add(returnCalendarView);

        //Uruchomienie obsługi aktualizacji czasu kalendarza i wyświetlania powiadomień w oddzielnym wątku.
        Thread updateTimeThread = new Thread("Kalendarz: aktualizacja czasu") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        returnCalendarView.setToday(LocalDate.now());
                        returnCalendarView.setTime(LocalTime.now());
                        LocalDate now = LocalDate.now();
                        LocalTime nowMinus5 = LocalTime.now().minusSeconds(5);
                        LocalTime nowPlus5 = LocalTime.now().plusSeconds(5);
                        Map<LocalDate, List<Entry<?>>> result = rentalReminders.findEntries(now,
                                now, ZoneId.systemDefault());
                        if(result.values().size() == 0){
                            return;
                        }
                        for(Entry<?> obj: result.get(now)) {
                            RentalReminder rentalReminder = (RentalReminder) obj.getUserObject();
                            if(obj.getEndTime().isBefore(nowPlus5) && obj.getEndTime().isAfter(nowMinus5)) {
                                mainController.showInfoDialog("Przypomnienie",
                                        "Czytelnik: " + rentalReminder.getRent().getREADER().getFirstName() + " " +rentalReminder.getRent().getREADER().getLastName() +
                                        " powinien zwrócić książkę: " + rentalReminder.getRent().getBOOK().getTitle() + " dnia " + rentalReminder.getRent().getDateOfReturn()
                                        );
                                rentalReminders.removeEntry(obj);
                                mainController.libraryController.deleteReminder(rentalReminder);
                            }
                        }
                    });
                    try {
                        // Aktualizacja co 10 sekund
                        sleep(10000);
                    } catch (InterruptedException e) {
                        mainController.showInfoDialog("Wątek aktualizacji kalendarza",e.getMessage());
                    }

                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        returnCalendarView.getCalendarSources().clear();
        returnCalendarView.getCalendarSources().add(rentsSource);

        loadRentsCalendar();
        loadRentRemindersCalendar();

        returnCalendarView.getSelections().addListener((InvalidationListener) changes -> {
            ObservableList<Entry<?>> selectedRent = FXCollections.observableArrayList(returnCalendarView.getSelections());
            if (selectedRent.size() == 1) {
                if(rentsCalendar.findEntries("").contains(selectedRent.get(0))){
                    returnBookButton.setDisable(false);
                    cancelReturnBookButton.setDisable(true);
                    extendRentalPeriodButton.setDisable(false);
                    addReminderButton.setDisable(false);
                    dateOfReminderField.setValue(null);
                    timeOfReminderField.setValue(null);
                } else {
                    returnBookButton.setDisable(true);
                    cancelReturnBookButton.setDisable(false);
                }
                if(rentalReminders.findEntries("").contains(selectedRent.get(0))) {
                    deleteReminderButton.setDisable(false);
                    cancelReturnBookButton.setDisable(true);
                }
            } else {
                returnBookButton.setDisable(true);
                cancelReturnBookButton.setDisable(true);
                extendRentalPeriodButton.setDisable(true);
                addReminderButton.setDisable(true);
                deleteReminderButton.setDisable(true);
            }
        });
    }

    /**
     * Wczytanie wypożyczeń z struktury danych do kalendarza wypożyczeń aktualnych rentsCalendar(nie zwrócone książki) i
     * kalendarza wypożyczeń archiwalnych rentsCalendarReturn(zwrócone książki).
     */
    void loadRentsCalendar() {
        rentsCalendarReturned.clear();
        rentsCalendar.clear();
        for (Rent rent : mainController.libraryController.getRents()) {
            Entry<Rent> rentEntry = new Entry<>("Książka: " + rent.getBOOK().getTitle() +
                    " \nCzytelnik: " + rent.getREADER().getFirstName() + " " + rent.getREADER().getLastName());
            rentEntry.setInterval(rent.getDateOfReturn());
            rentEntry.setUserObject(rent);
            if(rent.isReturned()) {
                rentsCalendarReturned.addEntry(rentEntry);
            } else {
                rentsCalendar.addEntry(rentEntry);
            }
        }
    }

    /**
     * Wczytanie przypomnień z struktury danych do kalendarza przypomnień rentalReminders.
     */
    void loadRentRemindersCalendar(){
        rentalReminders.clear();
        for(RentalReminder rentalReminder:mainController.libraryController.getRentalReminders()) {
            Entry<RentalReminder> reminderEntry = new Entry<>("Przypomnienie o zwrocie książki: " + rentalReminder.getRent().getBOOK().getTitle() +
                    " \nCzytelnik: " + rentalReminder.getRent().getREADER().getFirstName() + " " + rentalReminder.getRent().getREADER().getLastName() +
                    "\nData zwrotu: "+rentalReminder.getRent().getDateOfReturn());
            reminderEntry.setInterval(rentalReminder.getDateOfReminder());
            reminderEntry.setUserObject(rentalReminder);
            rentalReminders.addEntry(reminderEntry);
        }
    }

    /**
     * Zwrot książki przez czytelnika.
     */
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

    /**
     * Cofnięcie zwrotu książki przez czytelnika.
     */
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

    /**
     * Przedłużenie czasu wypożyczenia książki.
     */
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

    /**
     * Dodanie przypomnienia do struktury danych.
     */
    @FXML
    private void addReminder() {
        ObservableList<Entry<?>> currentRental = FXCollections.observableArrayList(returnCalendarView.getSelections());
        Rent rent = (Rent) currentRental.get(0).getUserObject();
        if(dateOfReminderField.getValue() == null){
            dateOfReminderField.setValue(rent.getDateOfReturn().toLocalDate().minusDays(2));
        }
        if(timeOfReminderField.getValue() == null) {
            timeOfReminderField.setValue(rent.getDateOfReturn().toLocalTime());
        }
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.addReminder(
                        rent,
                        LocalDateTime.of(dateOfReminderField.getValue(),timeOfReminderField.getValue())
                )
        );
        addReminderButton.setDisable(true);
        loadRentRemindersCalendar();
    }

    /**
     * Usunięcie przypomnienia ze struktury danych.
     */
    @FXML
    private void deleteReminder(){
        ObservableList<Entry<?>> currentReminder = FXCollections.observableArrayList(returnCalendarView.getSelections());
        RentalReminder rentalReminder = (RentalReminder) currentReminder.get(0).getUserObject();
        mainController.showInfoDialog(
                "Informacja",
                mainController.libraryController.deleteReminder(rentalReminder)
        );
        rentalReminders.removeEntry(currentReminder.get(0));
        deleteReminderButton.setDisable(true);
    }

}
