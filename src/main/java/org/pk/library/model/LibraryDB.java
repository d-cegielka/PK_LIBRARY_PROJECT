package org.pk.library.model;

import org.pk.library.view.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibraryDB {
    private Connection conn;
    private final Settings settings;

    public LibraryDB() throws SQLException {
        settings = Settings.getInstance();
        conn = this.connect();
    }

    /**
     * Ustanowienie połączenia z bazą danych
     * @return połączenie
     * @throws SQLException wyjątek SQL
     */
    private Connection connect() throws SQLException {
        Connection conn;
       /* String URL = "jdbc:mysql://srv31876.seohost.com.pl:3306/srv31876_library?serverTimezone=UTC";
        String user = "srv31876_pklibrary";
        String password = "WBbYzm0SJC";*/
        conn = DriverManager.getConnection(settings.getDatabaseURL(),
                settings.getDatabaseUsername(), settings.getDatabasePassword());
        return conn;
    }

    /**
     * Metoda usuwa tabele (jeśli istnieją) i tworzy wymagane tabele w bazie danych.
     * @throws SQLException wyjątek SQL
     * @throws IOException wyjątek otwarcia pliku IO
     */
    public void dropAndCreateTables() throws SQLException, IOException {
        if(conn.isClosed()) {
            conn = this.connect();
        }

        File sqlFile = new File("CREATETABLES.sql");
        String sql = new String(Files.readAllBytes(Paths.get(sqlFile.getPath())));
        String[] queries = sql.replaceAll("\\r\\n|\\r|\\n", " ").split(";");
        Statement st = conn.createStatement();
        for(String query: queries){
            st.execute(query);
        }
    }

    /**
     * Metoda sprawdzająca czy tabela istnieje w bazie danych.
     * @param table nazwa tabeli
     * @return wartość logiczna
     * @throws SQLException wyjątek SQL
     */
    public boolean checkIfTableExists(String table) throws SQLException {
        if(conn.isClosed()) {
            conn = this.connect();
        }
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null,null,table.toUpperCase(),null);
        return tables.next();
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param book obiekt klasy Book, który ma zostać dodany
     * @throws SQLException wyjątek SQL
     */
    public void insertBook(final Book book) throws SQLException {
        String sqlInsert = "INSERT INTO BOOKS (isbn, title, author, publisher, book_id) VALUES(?,?,?,?,?)";

        executeBookQuery(book, sqlInsert);
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param book obiekt klasy Book, który ma zostać zaktualizowany
     * @throws SQLException wyjątek SQL
     */
    public void updateBook(final Book book) throws SQLException {
        String sqlUpdate ="UPDATE BOOKS SET isbn=?, title=?, author=?, publisher=? WHERE book_id=?";

        executeBookQuery(book, sqlUpdate);
    }

    /**
     * Metoda wykonująca zapytanie SQL(INSERT lub UPDATE) na tabeli Books
     * @param book obiekt klasy Book do dodania/aktualizacji
     * @param sqlQuery zapytanie SQL
     * @throws SQLException wyjątek SQL
     */
    private void executeBookQuery(Book book, String sqlQuery) throws SQLException {
        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        preparedStatement.setString(1,book.getIsbn());
        preparedStatement.setString(2,book.getTitle());
        preparedStatement.setString(3,book.getAuthor());
        preparedStatement.setString(4,book.getPublisher());
        preparedStatement.setString(5,book.getBOOK_ID());
        preparedStatement.executeUpdate();
    }

    /**
     * Metoda usuwająca dane z wybranej tabeli w bazie danych
     * @param table nazwa tabeli
     * @param id identyfikator obiektu do usunięcia z tabeli
     * @return wartość logiczna
     * @throws SQLException wyjątek SQL
     */
    @SuppressWarnings("SqlResolve")
    public boolean deleteFromTable(String table, String id) throws SQLException {
        String sqlDelete = "DELETE FROM " + table + " WHERE " + table.substring(0, table.length() - 1).toLowerCase() + "_id=?";

        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlDelete);
        preparedStatement.setString(1,id);
        int result = preparedStatement.executeUpdate();
        return result == 1;
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param reader obiekt klasy Reader, który ma zostać dodany
     * @throws SQLException wyjątek SQL
     */
    public void insertReader(final Reader reader) throws SQLException {
        String sqlInsert = "INSERT INTO READERS (first_name, last_name, date_of_birth, phone_number, email, reader_id) VALUES(?,?,?,?,?,?)";

        executeReaderQuery(reader, sqlInsert);
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param reader obiekt klasy Reader, który ma zostać zaktualizowany
     * @throws SQLException wyjątek SQL
     */
    public void updateReader(final Reader reader) throws SQLException {
        String sqlUpdate ="UPDATE READERS SET first_name=?, last_name=?, date_of_birth=?, phone_number=?, email=? WHERE reader_id=?";

        executeReaderQuery(reader, sqlUpdate);
    }

    /**
     * Metoda wykonująca zapytanie SQL(INSERT lub UPDATE) na tabeli Readers
     * @param reader obiekt klasy Reader do dodania/aktualizacji
     * @param sqlQuery zapytanie SQL
     * @throws SQLException wyjątek SQL
     */
    private void executeReaderQuery(Reader reader, String sqlQuery) throws SQLException {
        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        preparedStatement.setString(1,reader.getFirstName());
        preparedStatement.setString(2,reader.getLastName());
        preparedStatement.setDate(3, Date.valueOf(reader.getDateOfBirth()));
        preparedStatement.setString(4,reader.getPhoneNumber());
        preparedStatement.setString(5,reader.getEmailAddress());
        preparedStatement.setString(6,reader.getREADER_ID());
        preparedStatement.executeUpdate();
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param rent obiekt klasy Rent, który ma zostać dodany
     * @throws SQLException wyjątek SQL
     */
    public void insertRent(final Rent rent) throws SQLException {
        String sqlInsert = "INSERT INTO RENTS (book, reader, date_of_rent, date_of_return, returned, rent_id) VALUES(?,?,?,?,?,?)";

        executeRentQuery(rent, sqlInsert);
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param rent obiekt klasy Rent, który ma zostać zaktualizowany
     * @throws SQLException wyjątek SQL
     */
    public void updateRent(final Rent rent) throws SQLException {
        String sqlUpdate = "UPDATE RENTS SET book=?, reader=?, date_of_rent=?, date_of_return=?, returned=? WHERE rent_id=?";

        executeRentQuery(rent, sqlUpdate);
    }

    /**
     * Metoda wykonująca zapytanie SQL(INSERT lub UPDATE) na tabeli Rents
     * @param rent obiekt klasy Rent do dodania/aktualizacji
     * @param sqlQuery zapytanie SQL
     * @throws SQLException wyjątek SQL
     */
    private void executeRentQuery(Rent rent, String sqlQuery) throws SQLException {
        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        preparedStatement.setString(1,rent.getBOOK().getBOOK_ID());
        preparedStatement.setString(2,rent.getREADER().getREADER_ID());
        preparedStatement.setTimestamp(3,Timestamp.valueOf(rent.getDateOfRent()));
        preparedStatement.setTimestamp(4,Timestamp.valueOf(rent.getDateOfReturn()));
        preparedStatement.setBoolean(5, rent.isReturned());
        preparedStatement.setString(6,rent.getRENT_ID());
        preparedStatement.executeUpdate();
    }

    /**
     * Metoda inicjalizująca zapytanie SQL
     * @param rentalReminder obiekt klasy RentalReminder, który ma zostać dodany
     * @throws SQLException wyjątek SQL
     */
    public void insertReminder(final RentalReminder rentalReminder) throws SQLException {
        String sqlInsert = "INSERT INTO RENTALREMINDERS (rent, date_of_reminder, rentalreminder_id) VALUES(?,?,?)";

        executeReminderQuery(rentalReminder, sqlInsert);
    }

    /**
     * Metoda wykonująca zapytanie SQL(INSERT lub UPDATE) na tabeli RentalReminders
     * @param rentalReminder obiekt klasy RentalReminders do dodania/aktualizacji
     * @param sqlQuery zapytanie SQL
     * @throws SQLException wyjątek SQL
     */
    private void executeReminderQuery(RentalReminder rentalReminder, String sqlQuery) throws SQLException {
        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        preparedStatement.setString(1,rentalReminder.getRent().getRENT_ID());
        preparedStatement.setTimestamp(2,Timestamp.valueOf(rentalReminder.getDateOfReminder()));
        preparedStatement.setString(3, rentalReminder.getREMINDER_ID());
        preparedStatement.executeUpdate();
    }


    /**
     * Metoda zwracająca listę książek pobranych z bazy danych.
     * @return lista książek
     * @throws SQLException wyjątek SQL
     */
    public List<Book> getBooksFromDB() throws SQLException {
        List<Book> books = new ArrayList<>();
        if(conn.isClosed()) {
            conn = this.connect();
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT isbn, title, author, publisher, book_id FROM BOOKS");

        while(rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
            String book_id = rs.getString("book_id");

            books.add(new Book(isbn,title,author,publisher,book_id));
        }
        rs.close();

        return books;
    }

    /**
     * Metoda zwracająca listę czytelników pobranych z bazy danych.
     * @return lista czytelników
     * @throws SQLException wyjątek SQL
     */
    public List<Reader> getReadersFromDB() throws SQLException {
        List<Reader> readers = new ArrayList<>();
        if(conn.isClosed()) {
            conn = this.connect();
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT first_name, last_name, date_of_birth, phone_number, email, reader_id FROM READERS");

        while(rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            LocalDate date_of_birth = rs.getDate("date_of_birth").toLocalDate();
            String phone_number = rs.getString("phone_number");
            String email = rs.getString("email");
            String reader_id = rs.getString("reader_id");

            readers.add(new Reader(first_name, last_name,date_of_birth,phone_number,email,reader_id));
        }
        rs.close();

        return readers;
    }

    /**
     * Metoda zwracająca listę wypożyczeń pobranych z bazy danych.
     * Metoda ta przyjmuje listy książek i czytelników w celu wyszukania obiektów po ID z bazy,
     * i przypisania tego samego obiektu, który znajduje się na liście tworzonemu wypożyczeniu.
     * @param books lista wypożyczonych ksiązek
     * @param readers lista czytelników, którzy wypożyczyli książkę
     * @return zwraca listę wypożyczeń
     * @throws SQLException wyjątek SQL
     */
    public List<Rent> getRentsFromDB(final List<Book> books, final List<Reader> readers) throws SQLException {
        List<Rent> rents = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT book, reader, date_of_rent, date_of_return, returned, rent_id FROM RENTS");

        while(rs.next()) {
            Book book = Library.findBookById(books, rs.getString("book"));
            Reader reader = Library.findReaderById(readers, rs.getString("reader"));
            LocalDateTime date_of_rent = rs.getTimestamp("date_of_rent").toLocalDateTime();
            LocalDateTime date_of_return = rs.getTimestamp("date_of_return").toLocalDateTime();
            boolean returned = rs.getBoolean("returned");
            String rent_id = rs.getString("rent_id");
            rents.add(new Rent(book, reader, date_of_rent, date_of_return, returned, rent_id));
        }
        rs.close();

        return rents;
    }

    /**
     * Metoda zwracająca listę przypomnień pobranych z bazy danych.
     * @param rents lista wypożyczeń
     * @return zwraca listę przypomnień
     * @throws SQLException wyjatek SQL
     */
    public List<RentalReminder> getRentalRemindersFromDB(final List<Rent> rents) throws SQLException{
        List<RentalReminder> rentalReminders = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rent, date_of_reminder, rentalreminder_id FROM RENTALREMINDERS");

        while (rs.next()){
            Rent rent = Library.findRentById(rents,rs.getString("rent"));
            LocalDateTime date_of_reminder = rs.getTimestamp("date_of_reminder").toLocalDateTime();
            String reminder_id = rs.getString("rentalreminder_id");
            rentalReminders.add(new RentalReminder(rent, date_of_reminder, reminder_id));
        }
        rs.close();

        return rentalReminders;
    }
}
