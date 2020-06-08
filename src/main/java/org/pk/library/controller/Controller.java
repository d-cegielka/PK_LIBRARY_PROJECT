package org.pk.library.controller;

import org.pk.library.model.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Kontroler
 */
public class Controller {
    /**
     * Klasa kontenerowa przechowująca strukturę danych biblioteki.
     */
    Library library;
    /**
     * Klasa umożliwająca operacje na bazie danych.
     */
    LibraryDB libraryDB;
    /**
     * Wzorzec walidacyjny ISBN.
     */
    Pattern isbnPattern;
    /**
     * Wzorzec walidacyjny dla numeru telefonu.
     */
    Pattern phoneNumberPattern;
    /**
     * Wzorzec walidacyjny dla adresu email.
     */
    Pattern emailAddressPattern;

    /**
     * Konstruktor kontrolera.
     * Tworzona jest struktura danych biblioteki.
     * @param connnectDB czy nawiązać połączenie z bazą danych
     * @throws SQLException wyjątek rzucany przez kontroler bazy danych
     * @throws IOException wyjątek IO
     */
    public Controller(boolean connnectDB) throws IOException, SQLException {
        library = new Library();
        if (connnectDB) {
            libraryDB = new LibraryDB();
            if (!checkExistsRequiredTables()) {
                libraryDB.dropAndCreateTables();
            }
            List<Book> books = new ArrayList<>(libraryDB.getBooksFromDB());
            List<Reader> readers = new ArrayList<>(libraryDB.getReadersFromDB());
            List<Rent> rents = new ArrayList<>(libraryDB.getRentsFromDB(books, readers));
            List<RentalReminder> rentalReminders = new ArrayList<>(libraryDB.getRentalRemindersFromDB(rents));
            library = new Library(books, readers, rents, rentalReminders);
        }
        isbnPattern = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
        phoneNumberPattern = Pattern.compile("^\\+[0-9]{1,3}[0-9]{4,14}(?:x.+)?$");
        emailAddressPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    /**
     * Metoda dodająca książkę do struktury danych.
     * @param isbn numer ISBN ksiązki
     * @param title tytuł książki
     * @param author autor książki
     * @param publisher wydawnictwo książki
     * @return komunikat dla interfejsu użytkownika
     */
    public final String addBook(final String isbn, final String title, final String author, final String publisher) {
            if (isbn.trim().isEmpty() || title.trim().isEmpty() || author.trim().isEmpty() || publisher.trim().isEmpty()) {
                return "Uzupełnij poprawnie wszystkie dane książki!";
            }
            if(!checkIsbn(isbn.trim())){
                //ISBN 978-0-596-52068-7 - valid
                //ISBN 11978-0-596-52068-7 - invalid
                return "Niepoprawny numer ISBN!";
            }
            Book addBook = new Book(isbn.trim(), title.trim(),author.trim(),publisher.trim());
            if(!library.addBook(addBook)){
                return "Nie udało się dodać książki!";
            }
            if(libraryDB != null) {
                try {
                    libraryDB.insertBook(addBook);
                } catch (SQLException se) {
                    library.removeBook(addBook);
                    return se.getMessage();
                }
            }
            return "Książka została dodana pomyślnie!";
    }

    /**
     * Metoda aktualizująca dane książki w strukturze danych.
     * @param updateBook obiekt klasy Book do aktualizacji
     * @param isbn numer ISBN ksiązki
     * @param title tytuł książki
     * @param author autor książki
     * @param publisher wydawnictwo książki
     * @return komunikat dla interfejsu użytkownika
     */
    public final String updateBook(final Book updateBook, final String isbn, final String title, final String author, final String publisher) {
        int changesNum = 0;
        if (isbn.trim().isEmpty() || title.trim().isEmpty() || author.trim().isEmpty() || publisher.trim().isEmpty()) {
            return "Uzupełnij poprawnie wszystkie dane książki!";
        }
        if (!isbn.trim().equals(updateBook.getIsbn())) {
            if (!checkIsbn(isbn.trim())) {
                //ISBN 978-0-596-52068-7 - valid
                //ISBN 11978-0-596-52068-7 - invalid
                return "Niepoprawny numer ISBN!";
            }
            updateBook.setIsbn(isbn.trim());
            changesNum++;
        }
        if (!title.trim().equals(updateBook.getTitle())) {
            updateBook.setTitle(title.trim());
            changesNum++;
        }
        if (!author.trim().equals(updateBook.getAuthor())) {
            updateBook.setAuthor(author.trim());
            changesNum++;
        }
        if (!publisher.trim().equals(updateBook.getPublisher())) {
            updateBook.setPublisher(publisher.trim());
            changesNum++;
        }

        if(changesNum > 0) {
            try {
                libraryDB.updateBook(updateBook);
            } catch (SQLException se) {
                return se.getMessage();
            }
            return "Książka została zaktualizowana pomyślnie!\n" +
                    "Ilość wprowadzonych zmian: " + changesNum;
        } else {
            return "Nie wprowadzono żadnych zmian!";
        }
    }

    /**
     * Metoda usuwająca książkę ze struktury danych.
     * @param removeBook obiekt klasy Book, który ma zostać usunięty
     * @return komunikat dla interfejsu użytkownika
     */
    public final String deleteBook(final Book removeBook) {
        try {
            if(libraryDB.deleteFromTable("BOOKS",removeBook.getBOOK_ID()) && library.removeBook(removeBook)){
                return "Książka została usunięta pomyślnie!";
            } else {
                return "Książka nie została usunięta!";
            }
        } catch (SQLException se) {
            return se.getMessage();
        }
    }

    /**
     * Metoda dodająca czytelnika do struktury danych.
     * @param firstName imię czytelnika
     * @param lastName nazwisko czytelnika
     * @param dateOfBirth data urodzenia czytelnika
     * @param phoneNumber numer kontaktowy czytelnika
     * @param emailAddress email czytelnika
     * @return komunikat dla interfejsu użytkownika
     */
    public final String addReader(final String firstName, final String lastName, final LocalDate dateOfBirth,
                                  final String phoneNumber, final String emailAddress){
        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() ||
                dateOfBirth== null || phoneNumber.trim().isEmpty() ||
                emailAddress.trim().isEmpty()) {
            return "Uzupełnij poprawnie wszystkie dane czytelnika!";
        }
        if(dateOfBirth.isAfter(LocalDate.now())){
            return "Wybrano datę urodzenia z przyszłości! Popraw dane!";
        }
        if(!checkPhoneNumber(phoneNumber.trim())) {
            return "Niepoprawny format numeru kontaktowego!";
        }
        if(!checkEmailAddress(emailAddress.trim())) {
            return "Niepoprawny format adresu e-mail!";
        }
        Reader addReader = new Reader(firstName.trim(),lastName.trim(),dateOfBirth,
                phoneNumber.trim(),emailAddress.trim());
        if(!library.addReader(addReader)){
            return "Nie udało się dodać czytelnika!";
        }

        if(libraryDB != null) {
            try {
                libraryDB.insertReader(addReader);
            } catch (SQLException se) {
                library.removeReader(addReader);
                return se.getMessage();
            }
        }
        return "Czytelnik został dodany pomyślnie!";
    }

    /**
     *  Metoda aktualizująca dane czytelnika w strukturze danych.
     * @param updateReader obiekt klasy Reader do aktualizacji
     * @param firstName imię czytelnika
     * @param lastName nazwisko czytelnika
     * @param dateOfBirth data urodzenia czytelnika
     * @param phoneNumber numer kontaktowy czytelnika
     * @param emailAddress email czytelnika
     * @return komunikat dla interfejsu użytkownika
     */
    public final String updateReader(final Reader updateReader, final String firstName, final String lastName, final LocalDate dateOfBirth,
                                     final String phoneNumber, final String emailAddress) {
        int changesNum = 0;
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || dateOfBirth == null || phoneNumber.trim().isEmpty() || emailAddress.trim().isEmpty()) {
            return "Uzupełnij poprawnie wszystkie dane czytelnika!";
        }
        if (!firstName.trim().equals(updateReader.getFirstName())) {
            updateReader.setFirstName(firstName.trim());
            changesNum++;
        }
        if (!lastName.trim().equals(updateReader.getLastName())) {
            updateReader.setLastName(lastName.trim());
            changesNum++;
        }
        if (!dateOfBirth.equals(updateReader.getDateOfBirth())) {
            if(dateOfBirth.isAfter(LocalDate.now())) {
                return "Wybrano datę urodzenia z przyszłości! Popraw dane!";
            }
            updateReader.setDateOfBirth(dateOfBirth);
            changesNum++;
        }
        if(!phoneNumber.trim().equals(updateReader.getPhoneNumber())) {
            if(!checkPhoneNumber(phoneNumber.trim())) {
                return "Niepoprawny format numeru kontaktowego!";
            }
            updateReader.setPhoneNumber(phoneNumber.trim());
            changesNum++;
        }

        if(!emailAddress.trim().equals(updateReader.getEmailAddress())) {
            if(!checkEmailAddress(emailAddress.trim())) {
                return "Niepoprawny format adresu e-mail!";
            }
            updateReader.setEmailAddress(emailAddress.trim());
            changesNum++;
        }

        if(changesNum > 0) {
            try {
                libraryDB.updateReader(updateReader);
            } catch (SQLException se) {
                return se.getMessage();
            }
            return "Dane czytelnika zostały zaktualizowane pomyślnie!\n" +
                    "Ilość wprowadzonych zmian: " + changesNum;
        } else {
            return "Nie wprowadzono żadnych zmian!";
        }
    }

    /**
     * Metoda usuwająca czytelnika ze struktury danych.
     * @param removeReader obiekt klasy Reader, który ma zostać usunięty
     * @return komunikat dla interfejsu użytkownika
     */
    public final String deleteReader(final Reader removeReader) {
        try {
            if(libraryDB.deleteFromTable("READERS",removeReader.getREADER_ID()) && library.removeReader(removeReader)){
                return "Czytelnik został usunięty pomyślnie!";
            } else {
                return "Czytelnik nie został usunięty!";
            }
        } catch (SQLException se) {
            return se.getMessage();
        }
    }

    /**
     * Metoda sprawdzająca czy podany ciąg znaków jest liczbą
     * @param string ciąg znaków
     * @return wartość logiczna
     */
    public boolean isNumeric(String string) {
       return string.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Metoda dodająca wypożyczenie do struktury danych.
     * @param book obiekt książki
     * @param reader obiekt czytelnika
     * @param rentDate data wypożyczenia książki
     * @param rentalPeriod ilość dni wypożycznia książki
     * @return komunikat dla interfejsu użytkownika
     */
    public final String addRent(final Book book, final Reader reader, final LocalDateTime rentDate, final String rentalPeriod){
        if(book == null || reader == null || rentDate == null || !isNumeric(rentalPeriod)) {
            return "Uzupełnij poprawnie wszystkie dane wypożyczenia!";
        }
        LocalDateTime returnDate = rentDate.plusDays(Integer.parseInt(rentalPeriod));
        if(ChronoUnit.DAYS.between(rentDate, LocalDateTime.now()) > 7) {
            return "Błędna data wypożyczenia! Data wypożyczenia przed " + LocalDateTime.now().minusDays(7).toString() + " nie jest akceptowalna!";
        }
        if(returnDate.isBefore(rentDate)) {
            return "Błędna data zwrotu! Data zwrotu nie może być wcześniejsza niż data wypożyczenia";
        }
        if(!checkBookIsAvailable(book)) {
            return "Wybrana książka jest niedostępna!";
        }

        Rent addRent = new Rent(book,reader,rentDate,returnDate,false);
        if(!library.addRent(addRent)) {
            return "Nie udało się dodać wypożyczenia";
        }

        if(libraryDB != null) {
            try {
                libraryDB.insertRent(addRent);
            } catch (SQLException se) {
                library.removeRent(addRent);
                return se.getMessage();
            }
        }
        return "Książka została wypożyczona pomyślnie!";
    }

    /**
     * Metoda aktualizująca wypożyczenie w strukturze danych.
     * @param updateRent obiekt klasy Rent do aktualizacji
     * @param rentDate  data wypożyczenia książki
     * @param rentalPeriod ilość dni o ile przedłużyć wypożyczenie
     * @return komunikat do interfejsu użytkownika
     */
    public final String updateRent(final Rent updateRent, final LocalDateTime rentDate, final String rentalPeriod) {
        int changesNum = 0;
        if (rentDate == null || !isNumeric(rentalPeriod)) {
            return "Uzupełnij poprawnie wszystkie dane wypożyczenia!";
        }
        if (updateRent.isReturned()) {
            return "Nie można edytować danych zakończonego wypożyczenia!";
        }
        LocalDateTime returnDate = rentDate.plusDays(Integer.parseInt(rentalPeriod));

        if (rentDate.compareTo(updateRent.getDateOfRent()) != 0) {
            if (ChronoUnit.DAYS.between(rentDate, LocalDateTime.now()) > 7) {
                return "Błędna data wypożyczenia! Data wypożyczenia przed " + LocalDateTime.now().minusDays(7).toString() + " nie jest akceptowalna!";
            }
            updateRent.setDateOfRent(rentDate);
            changesNum++;
        }
        if (returnDate.compareTo(updateRent.getDateOfReturn()) != 0) {
            if (returnDate.isBefore(rentDate)) {
                return "Błędna data zwrotu! Data zwrotu nie może być wcześniejsza niż data wypożyczenia";
            }
            updateRent.setDateOfReturn(returnDate);
            changesNum++;
        }
        if (changesNum > 0) {
            try {
                libraryDB.updateRent(updateRent);
            } catch (SQLException se) {
                return se.getMessage();
            }
            return "Dane wypożyczenia zostały zaktualizowane pomyślnie!\n" +
                    "Ilość wprowadzonych zmian: " + changesNum;
        } else {
            return "Nie wprowadzono żadnych zmian!";
        }
    }

    /**
     * Metoda do przedłużenia czasu wypożyczenia
     * @param rent obiekt klasy Rent do przedłużenia wypożyczenia
     * @param numOfDaysExtendRentPeriod ilość dni o ile przedłużyć wypożyczenie
     * @return komunikat do interfejsu użytkownika
     */
    public final String extendRentalPeriod(final Rent rent, final String numOfDaysExtendRentPeriod) {
        int changesNum = 0;
        if(rent == null || !isNumeric(numOfDaysExtendRentPeriod)) {
            return "Nie wybrano książki do przedłużenia wypożyczenia!";
        }
        int numOfDays = Integer.parseInt(numOfDaysExtendRentPeriod);
        if(rent.getDateOfReturn().plusDays(numOfDays) != rent.getDateOfReturn()) {
            rent.setDateOfReturn(rent.getDateOfReturn().plusDays(numOfDays));
            changesNum++;
        }
        if(changesNum > 0) {
            try {
                libraryDB.updateRent(rent);
            } catch (SQLException se) {
                rent.setDateOfReturn(rent.getDateOfReturn().minusDays(numOfDays));
                return se.getMessage();
            }
            return "Data zwrotu wypożyczenia została przedłużona o " + numOfDaysExtendRentPeriod + " dni" +
                    "\nNowa data zwrotu: " + rent.getDateOfReturn().toString();
        } else {
            return "Nie wprowadzono żadnych zmian!";
        }
    }

    /**
     * Metoda zwracająca / cofająca zwrot książki
     * @param rent obiekt klasy Rent z wypożyczeniem
     * @param op typ operacji
     * @return komunikat do interfejsu użytkownika
     */
    public final String returnBook(final Rent rent, final boolean op) {
        if(rent == null) {
            return "Nie wybrano książki do zwrotu!";
        }
        if(rent.isReturned() == op){
            if(op) {
                return "Książka "+ rent.getBOOK().getTitle()+" została zwrócona wcześniej!";
            } else{
                return "Nie można cofnąć zwrotu! Książka "+ rent.getBOOK().getTitle()+" nie została zwrócona!";
            }
        }
        rent.setReturned(op);
        try {
            libraryDB.updateRent(rent);
        } catch (Exception e) {
            rent.setReturned(!op);
            return e.getMessage();
        }
        if(op){
            return "Książka "+ rent.getBOOK().getTitle()+" została zwrócona pomyślnie!";
        } else {
            return "Zwrot książki "+ rent.getBOOK().getTitle()+" został wycofany pomyślnie!";
        }
    }

    /**
     * Metoda dodająca przypomnienie o zwrocie książki
     * @param rent obiekt klasy Rent z wypożyczeniem
     * @param dateOfReminder data i czas przypomnienia
     * @return komunikat do interfejsu użytkownika
     */
    public final String addReminder(final Rent rent, final LocalDateTime dateOfReminder) {
        if(rent == null || dateOfReminder == null) {
            return "Uzupełnij poprawnie wszystkie dane do ustawienia przypomnienia!";
        }
        if(ChronoUnit.DAYS.between(dateOfReminder,rent.getDateOfReturn()) <= -1) {
            return "Błedna data przypomnienia! \nData zwrotu książki: " +
                    rent.getDateOfReturn().toString();
        }
        RentalReminder rentalReminder = new RentalReminder(rent,dateOfReminder);
        if(!library.addRentalReminder(rentalReminder)){
            return "Nie udało się dodać przypomnienia!";
        }

        if(libraryDB != null) {
            try {
                libraryDB.insertReminder(rentalReminder);
            } catch (SQLException se) {
                library.removeRentalReminder(rentalReminder);
                return se.getMessage();
            }
        }
        return "Przypomnienie zostało dodane poprawnie!";
    }

    /**
     * Metoda usuwająca przypomnienie ze struktury danych.
     * @param rentalReminder obiekt klasy RentalReminder, który ma zostać usunięty
     * @return komunikat dla interfejsu użytkownika
     */
    public final String deleteReminder(final RentalReminder rentalReminder) {
        try {
            if(libraryDB.deleteFromTable("RENTALREMINDERS",rentalReminder.getREMINDER_ID()) && library.removeRentalReminder(rentalReminder)){
                return "Przypomnienie zostało usunięte pomyślnie!";
            } else {
                return "Przypomnienie nie zostało usunięte!";
            }
        } catch (SQLException se) {
            return se.getMessage();
        }
    }

    /**
     * Import danych z struktury danych bibloteki(listy) do bazy danych.
     * @return komunikat dla interfejsu użytkownika
     */
    public final String importDataFromListsToDB() {
        try {
            libraryDB.dropAndCreateTables();
            for(Book book : getBooks()) {
                libraryDB.insertBook(book);
            }
            for(Reader reader : getReaders()) {
                libraryDB.insertReader(reader);
            }
            for(Rent rent : getRents()) {
                libraryDB.insertRent(rent);
            }
            for(RentalReminder rentalReminder : getRentalReminders()) {
                libraryDB.insertReminder(rentalReminder);
            }
        }
        catch (SQLException | IOException se) {
            return se.getMessage();
        }
        return "Eksport danych do bazy danych zakończony pomyślnie.";
    }

    /**
     * Import danych z pliku XML do struktury danych biblioteki oraz bazy danych.
     * @param filePath ścieżka do pliku XML
     * @return komunikat dla interfejsu użytkownika
     */
    public final String importDataFromXML(String filePath) {
        LibraryXML libraryXML = new LibraryXML();
        try {
            libraryDB.dropAndCreateTables();
            library = libraryXML.readDataFromXML(filePath);
            return importDataFromListsToDB();
        } catch (SQLException | IOException | XMLStreamException se) {
            return se.getMessage();
        }
    }

    /**
     * Eksport struktury danych biblioteki do pliku XML.
     * @param filePath scieżka pliku XML do zapisu
     * @return komunikat dla interfejsu użytkownika
     */
    public final String exportDataToXML(String filePath){
        LibraryXML libraryXML = new LibraryXML();
        try {
            libraryXML.saveDataToXML(library, filePath);
            return "Dane zostały wyeksportowane pomyślnie! \nŚcieżka do pliku: " + filePath;
        } catch (XMLStreamException | IOException se) {
            return se.getMessage();
        }
    }

    /**
     * Metoda sprawdzająca czy w bazie danych istnieją wymagane tabele.
     * @return wartość logiczna
     * @throws SQLException wyjątek rzucany przez kontroler bazy danych
     */
    public boolean checkExistsRequiredTables() throws SQLException {
        return libraryDB.checkIfTableExists("books") &&
                libraryDB.checkIfTableExists("readers") &&
                libraryDB.checkIfTableExists("rents") &&
                libraryDB.checkIfTableExists("rentalreminders");
    }

    /**
     * Walidacja numeru ISBN
     * @param isbn numer ISBN
     * @return zwraca informację o poprawności walidacji
     */
    private boolean checkIsbn(final String isbn) {
        return isbnPattern.matcher(isbn).matches();
    }

    /**
     * Walidacja numeru kontaktowego
     * @param phoneNumber numer kontaktowy
     * @return zwraca informację o poprawności walidacji
     */
    private boolean checkPhoneNumber(final String phoneNumber){
        return phoneNumberPattern.matcher(phoneNumber).matches();
    }

    /**
     * Walidacja adresu e-mail
     * @param emailAddress adres e-mail
     * @return zwraca informację o poprawności walidacji
     */
    private boolean checkEmailAddress(final String emailAddress) {
        return emailAddressPattern.matcher(emailAddress).matches();
    }

    /**
     * Metoda sprawdzająca dostępność książki do wypożyczenia.
     * @param book obiekt klasy Book, którego dostępność ma być sprawdzona
     * @return informacja o dostępności książki
     */
    private boolean checkBookIsAvailable(final Book book){
        return library.getRents().stream().filter(rent ->{
            if(book.equals(rent.getBOOK()) && !rent.isReturned()) {
                return true;
            }
            return false;
        }
        ).findAny().orElse(null) == null;
    }

    /**
     * Metoda zwracająca listę książek.
     * @return lista książek
     */
    public final List<Book> getBooks() {
        return library.getBooks();
    }

    /**
     * Metoda zwracająca listę czytelników.
     * @return lista czytelników
     */
    public final List<Reader> getReaders() {
        return library.getReaders();
    }

    /**
     * Metoda zwracająca listę wypożyczeń.
     * @return lista wypożyczeń
     */
    public final List<Rent> getRents() {
        return library.getRents();
    }

    /**
     * Metoda zwracająca listę przypomnień.
     * @return lista przypomnień
     */
    public final List<RentalReminder> getRentalReminders() {
        return library.getRentalReminders();
    }
}
