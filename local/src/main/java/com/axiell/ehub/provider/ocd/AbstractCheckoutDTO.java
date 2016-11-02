package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractCheckoutDTO {
    private String transactionId;
    private String titleId;
    private String patronId;
    private String libraryId;
    private String isbn;
    private boolean canRenew;

    public String getPatronId() {
        return patronId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public boolean getCanRenew() {
        return canRenew;
    }

    public String getTitleId() {
        return titleId;
    }
}
