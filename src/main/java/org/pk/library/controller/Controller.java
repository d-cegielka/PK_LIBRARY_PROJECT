package org.pk.library.controller;

import org.pk.library.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Controller {
    Library library;
    LibraryDB libraryDB;
    Pattern isbnPattern;
    Pattern phoneNumberPattern;
    Pattern emailAddressPattern;

    /**
     * Konstruktor bezparametrowy kontrolera.
     * Tworzona jest struktura danych biblioteki.
     * @throws SQLException wyjątek rzuacany przez kontroler bazy danych
     */
    public Controller() throws SQLException {
        library = new Library();
        libraryDB = new LibraryDB();
        List<Book> books = new ArrayList<>(libraryDB.getBooksFromDB());
        List<Reader> readers = new ArrayList<>(libraryDB.getReadersFromDB());
        List<Rent> rents = new ArrayList<>(libraryDB.getRentsFromDB(books,readers));
        library = new Library(books,readers,rents);
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
    public static boolean isNumeric(String string) {
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
}
