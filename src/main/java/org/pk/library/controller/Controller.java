package org.pk.library.controller;

import org.pk.library.model.Library;

public class Controller {
    Library library;

    public void addBook(String isbn, String title, String author, String publisher) throws Exception {
        if(isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
            throw new Exception("Uzupełnij wszystkie pola!");
        }

    }
}
