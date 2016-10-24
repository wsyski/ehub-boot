package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.Md5Function;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatronDTO {
    private static final String NA = "N/A";
    public static final String EMAIL_DOMAIN = "@axiell.com";
    private String patronId;
    private String libraryId;
    private String libraryCardNumber;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String postalCode;

    public PatronDTO() {
    }

    public PatronDTO(final Patron patron, final String libraryId) {
        this.libraryId = libraryId;
        this.libraryCardNumber = patron.getLibraryCard();
        this.userName = patron.getLibraryCard();
        this.password = Md5Function.md5Hex(patron.getLibraryCard());
        this.email = makeEmail();
        this.firstName = NA;
        this.lastName = NA;
        this.postalCode = NA;
    }

    private String makeEmail() {
        return userName + EMAIL_DOMAIN;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getLibraryId() {
        return libraryId;
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
