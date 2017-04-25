package com.axiell.auth;

import com.axiell.auth.util.SHA512Function;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Patron {
    private final Builder builder;

    private Patron(final Builder builder) {
        this.builder = builder;
    }

    private String generateId() {
        if (hasLibraryCard()) {
            return SHA512Function.sha512Hex(builder.getLibraryCard());
        } else {
            return null;
        }
    }

    public boolean hasId() {
        return !isBlank(builder.getId());
    }

    public String getId() {
        return isBlank(builder.getId()) ? generateId() : builder.getId();
    }

    public boolean hasLibraryCard() {
        return !isBlank(getLibraryCard());
    }

    public String getLibraryCard() {
        return builder.getLibraryCard();
    }

    public boolean hasPin() {
        return !isBlank(builder.getPin());
    }

    public String getPin() {
        return builder.getPin();
    }

    public boolean hasEmail() {
        return !isBlank(builder.getEmail());
    }

    public String getEmail() {
        return builder.getEmail();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class Builder {
        private String libraryCard;
        private String pin;
        private String id;
        private String email;
        private Long arenaUserId;
        private String name;

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder libraryCard(final String libraryCard) {
            this.libraryCard = libraryCard;
            return this;
        }

        public Builder pin(final String pin) {
            this.pin = pin;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder arenaUserId(final Long arenaUserId) {
            this.arenaUserId = arenaUserId;
            return this;
        }

        public String getId() {
            return id;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public String getLibraryCard() {
            return libraryCard;
        }

        public String getPin() {
            return pin;
        }


        public String getEmail() {
            return email;
        }

        public Long getArenaUserId() {
            return arenaUserId;
        }

        public String getName() {
            return name;
        }

        public Patron build() {
            return new Patron(this);
        }
    }
}
