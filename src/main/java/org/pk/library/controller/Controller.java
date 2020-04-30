package org.pk.library.controller;

import org.pk.library.model.Book;
import org.pk.library.model.Library;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Controller {
    Library library;
    Pattern isbnPattern;

    public Controller() {
        library = new Library();
        isbnPattern = Pattern.compile("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$");
    }

    public void addBook(String isbn, String title, String author, String publisher) throws Exception {
        if(isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
            throw new Exception("Uzupełnij wszystkie pola!");
        }
        if(!(isbnPattern.matcher(isbn)).matches())
        {
            //ISBN 978-0-596-52068-7 - valid
            //ISBN 11978-0-596-52068-7 - invalid
            throw new Exception("Niepoprawny numer ISBN!");
        }
        if(!library.addBook(new Book(isbn, title, author, publisher))){
            throw new Exception("Nie udało się dodać książki!");
        }
    }

   /* public void addReader(String name, String lastName, LocalDate dateOfBirth, String phoneNumber, String emailAddress) throws Exception {
        if(name.isEmpty() || lastName.isEmpty() || dateOfBirth.toString().isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty()) {
            throw new Exception("Uzupełnij wszystkie pola!");
        }
        //library.addBook();
    }*/


}
