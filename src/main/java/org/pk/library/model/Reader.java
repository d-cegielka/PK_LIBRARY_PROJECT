package org.pk.library.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Reader extends RecursiveTreeObject<Reader> {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private final String READER_ID;

    public Reader(String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String emailAddress) {
        this.READER_ID = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Reader(String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String emailAddress, String READER_ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.READER_ID = READER_ID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Reader{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", readerID='").append(READER_ID).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader)) return false;
        Reader reader = (Reader) o;
        return Objects.equals(firstName, reader.firstName) &&
                Objects.equals(lastName, reader.lastName) &&
                Objects.equals(dateOfBirth, reader.dateOfBirth) &&
                Objects.equals(phoneNumber, reader.phoneNumber) &&
                Objects.equals(emailAddress, reader.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, phoneNumber, emailAddress);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getREADER_ID() {
        return READER_ID;
    }
}
