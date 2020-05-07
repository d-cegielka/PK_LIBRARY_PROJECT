package org.pk.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ReaderTest {

    Reader reader1;

    @BeforeEach
    void setUp() {
        reader1 = new Reader("Jan","Kowalski", LocalDate.of(1990,1,12),"666777888","jan.kowalski@gmail.com");
    }

    @Test
    void testToString() {
        assertThat(reader1.toString()).contains("Jan","Kowalski","1990-01-12");
    }

    @Test
    void getName() {
        assertEquals(reader1.getFirstName(),"Jan");
    }

    @Test
    void setName() {
        reader1.setFirstName("Wojciech");
        assertEquals(reader1.getFirstName(),"Wojciech");
    }

    @Test
    void getLastName() {
        assertEquals(reader1.getLastName(),"Kowalski");
    }

    @Test
    void setLastName() {
        reader1.setLastName("Nowak");
        assertEquals(reader1.getLastName(),"Nowak");
    }

    @Test
    void getDateOfBirth() {
        assertEquals(reader1.getDateOfBirth(),LocalDate.of(1990,1,12));
    }

    @Test
    void setDateOfBirth() {
        reader1.setDateOfBirth(LocalDate.of(1990,1,15));
        assertEquals(reader1.getDateOfBirth(),LocalDate.of(1990,1,15));
    }

    @Test
    void getPhoneNumber() {
        assertEquals(reader1.getPhoneNumber(),"666777888");
    }

    @Test
    void setPhoneNumber() {
        reader1.setPhoneNumber("887789584");
        assertEquals(reader1.getPhoneNumber(),"887789584");
    }

    @Test
    void getEmailAddress() {
        assertEquals(reader1.getEmailAddress(),"jan.kowalski@gmail.com");
    }

    @Test
    void setEmailAddress() {
        reader1.setEmailAddress("test@example.com");
        assertEquals(reader1.getEmailAddress(),"test@example.com");
    }

}