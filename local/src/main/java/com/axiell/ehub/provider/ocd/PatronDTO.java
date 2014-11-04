package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.patron.Patron;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"libraryPin", "libraryId"})
public class PatronDTO {
    private static final String NA = "N/A";
    private static final String EMAIL_DOMAIN = "@dummy.se";
    private String patronId;
    private String libraryCardNumber;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String postalCode;

    public PatronDTO() {
    }

    PatronDTO(Patron patron) {
        libraryCardNumber = patron.getLibraryCard();
        userName = patron.getLibraryCard();
        password = patron.getPin();
        email = makeEmail();
        firstName = NA;
        lastName = NA;
        postalCode = NA;
    }

    private String makeEmail() {
        return userName + EMAIL_DOMAIN;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
