package com.axiell.ehub.patron;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;

import static com.axiell.ehub.util.SHA512Function.sha512Hex;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Patron {
    private final String id;
    private final String libraryCard;
    private final String pin;

    private Patron(String id, String libraryCard, String pin) {
        this.libraryCard = libraryCard;
        this.pin = pin;
        this.id = isBlank(id) ? generateId() : id;
    }

    private String generateId() {
        if (hasLibraryCard())
            return sha512Hex(libraryCard);
        else
            return null;
    }

    public boolean hasId() {
        return !isBlank(id);
    }

    public String getId() {
        // TODO: throw exception if null?
        return id;
    }

    public boolean hasLibraryCard() {
        return !isBlank(libraryCard);
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

        public Patron build() {
            return new Patron(id, libraryCard, pin);
        }
    }
}
