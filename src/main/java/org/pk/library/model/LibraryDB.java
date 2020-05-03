package org.pk.library.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDB {
    private final String URL = "jdbc:mysql://srv31876.seohost.com.pl:3306/srv31876_library?serverTimezone=UTC";
    private final String user =  "srv31876_pklibrary";
    private final String password = "WBbYzm0SJC";
    private Connection conn;

    public LibraryDB() throws SQLException {
        conn = this.connect();
    }

    private Connection connect() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(URL,user,password);
        return conn;
    }

    public void insertBook(final Book book) throws SQLException {
        String sqlInsert = "INSERT INTO BOOKS (isbn, title, author, publisher, book_id) VALUES(?,?,?,?,?)";

        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert);
        preparedStatement.setString(1,book.getIsbn());
        preparedStatement.setString(2,book.getTitle());
        preparedStatement.setString(3,book.getAuthor());
        preparedStatement.setString(4,book.getPublisher());
        preparedStatement.setString(5,book.getBookID());
        preparedStatement.executeUpdate();
    }

    public void updateBook(final Book book) throws SQLException {
        String sqlUpdate ="UPDATE BOOKS SET isbn=?, title=?, author=?, publisher=? WHERE book_id=?";

        if(conn.isClosed()) {
            conn = this.connect();
        }
        PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);
        preparedStatement.setString(1,book.getIsbn());
        preparedStatement.setString(2,book.getTitle());
        preparedStatement.setString(3,book.getAuthor());
        preparedStatement.setString(4,book.getPublisher());
        preparedStatement.setString(5,book.getBookID());
        preparedStatement.executeUpdate();
    }

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

    public List<Reader> getReadersFromDB() throws SQLException {
        List<Reader> readers = new ArrayList<>();
        /*Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT isbn, title, author, publisher, book_id FROM books");

        while(rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
            String book_id = rs.getString("book_id");

            books.add(new Book(isbn,title,author,publisher,book_id));
        }
        rs.close();
*/
        return readers;
    }

    public List<Rent> getRentsFromDB() throws SQLException {
        List<Rent> rents = new ArrayList<>();
    /*    Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT isbn, title, author, publisher, book_id FROM books");

        while(rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
            String book_id = rs.getString("book_id");

            books.add(new Book(isbn,title,author,publisher,book_id));
        }
        rs.close();*/

        return rents;
    }

}
