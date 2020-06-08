package org.pk.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


class BookTest {
    Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("5869782","Harry Potter", "J.K Rowling", "PWN");
    }

    @Test
    void testToString() {
        assertThat(testBook.toString()).contains("5869782", "PWN");
    }

    @Test
    void getIsbn() {
        assertEquals("5869782",testBook.getIsbn());
    }

    @Test
    void setIsbn() {
        testBook.setIsbn("9978695");
        assertEquals("9978695",testBook.getIsbn());
    }

    @Test
    void getTitle() {
        assertEquals("Harry Potter",testBook.getTitle());
    }

    @Test
    void setTitle() {
        testBook.setTitle("Opowieści z Narnii");
        assertEquals("Opowieści z Narnii",testBook.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("J.K Rowling",testBook.getAuthor());
    }

    @Test
    void setAuthor() {
        testBook.setAuthor("C.S. Lewis");
        assertEquals("C.S. Lewis",testBook.getAuthor());
    }

    @Test
    void getPublisher() {
        assertEquals("PWN",testBook.getPublisher());
    }

    @Test
    void setPublisher() {
        testBook.setPublisher("Helion");
        assertEquals("Helion",testBook.getPublisher());
    }

}