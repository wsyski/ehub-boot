package com.axiell.ehub.patron;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;
import org.apache.commons.lang3.StringUtils;

public class Patron {
    private String id;
    private final String libraryCard;
    private final String pin;

    private Patron(String id, String libraryCard, String pin) {
        this.id = id;
        this.libraryCard = libraryCard;
        this.pin = pin;
    }

    public boolean hasId() {
        return !StringUtils.isBlank(id);
    }

    public String getId() {
        // TODO: throw exception if null?
        return id;
    }

    public boolean hasLibraryCard() {
        return !StringUtils.isBlank(libraryCard);
    }

    public String getLibraryCard() {
        if (hasLibraryCard())
            return libraryCard;
        throw new UnauthorizedException(ErrorCause.MISSING_LIBRARY_CARD);
    }

    public String getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Patron{id=" + id + ", libraryCard=" + libraryCard + ", pin=" + pin + "}";
    }

    public static class Builder {
        private final String libraryCard;
        private final String pin;
        private String id;

        public Builder(String libraryCard, String pin) {
            this.libraryCard = libraryCard;
            this.pin = pin;
        }

        public Builder id(String value) {
            id = value;
            return this;
        }

        public boolean hasCardButNoId() {
            return id == null && libraryCard != null;
        }

        public Patron build() {
            return new Patron(id, libraryCard, pin);
        }
    }
}
