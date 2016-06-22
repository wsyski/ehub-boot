package com.axiell.ehub.patron;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import static com.axiell.ehub.util.SHA512Function.sha512Hex;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Patron {
    private final String id;
    private final String libraryCard;
    private final String pin;
    private final String email;

    private Patron(final String id, final String libraryCard, final String pin, final String email) {
        this.libraryCard = libraryCard;
        this.pin = pin;
        this.email = email;
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

    public boolean hasPin() {
        return !isBlank(pin);
    }
    public String getPin() {
        return pin;
    }


    public boolean hasEmail() {
        return !isBlank(email);
    }

    public String getEmail() {
        if (hasEmail())
            return email;
        throw new UnauthorizedException(ErrorCause.MISSING_EMAIL);
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class Builder {
        private final String libraryCard;
        private final String pin;
        private String id;
        private String email;

        public Builder(String libraryCard, String pin) {
            this.libraryCard = libraryCard;
            this.pin = pin;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Patron build() {
            return new Patron(id, libraryCard, pin, email);
        }
    }
}
