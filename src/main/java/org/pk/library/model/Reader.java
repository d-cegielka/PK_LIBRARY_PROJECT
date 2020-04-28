package org.pk.library.model;

import java.util.Date;
import java.util.Objects;

public class Reader {
    private String name;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String emailAddress;

    public Reader(String name, String lastName, Date dateOfBirth, String phoneNumber, String emailAddress) {
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Reader{");
        sb.append("name='").append(name).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader)) return false;
        Reader reader = (Reader) o;
        return Objects.equals(name, reader.name) &&
                Objects.equals(lastName, reader.lastName) &&
                Objects.equals(dateOfBirth, reader.dateOfBirth) &&
                Objects.equals(phoneNumber, reader.phoneNumber) &&
                Objects.equals(emailAddress, reader.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, dateOfBirth, phoneNumber, emailAddress);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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
}
