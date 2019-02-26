package com.axiell.authinfo;

import com.axiell.authinfo.util.SHA512Function;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
        return isNotBlank(builder.getId());
    }

    public String getId() {
        return isBlank(builder.getId()) ? generateId() : builder.getId();
    }

    public boolean hasLibraryCard() {
        return isNotBlank(getLibraryCard());
    }

    public String getLibraryCard() {
        return builder.getLibraryCard();
    }

    public boolean hasPin() {
        return isNotBlank(builder.getPin());
    }

    public String getPin() {
        return builder.getPin();
    }

    public boolean hasEmail() {
        return isNotBlank(builder.getEmail());
    }

    public String getEmail() {
        return builder.getEmail();
    }

    public void setEmail(final String email) {
        builder.email = email;
    }

    public String getName() {
        return builder.getName();
    }

    public boolean hasName() {
        return isNotBlank(builder.getName());
    }

    public LocalDate getBirthDate() {
        return builder.getBirthDate();
    }

    public boolean hasBirthDate() {
        return builder.getBirthDate() != null;
    }

    public Long getArenaUserId() {
        return builder.getArenaUserId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Patron patron = (Patron) o;

        return new EqualsBuilder()
                .append(builder, patron.builder)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(builder)
                .toHashCode();
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
        private LocalDate birthDate;

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

        public Builder birthDate(final LocalDate birthDate) {
            this.birthDate = birthDate;
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

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public Patron build() {
            return new Patron(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Builder builder = (Builder) o;

            return new EqualsBuilder()
                    .append(getLibraryCard(), builder.getLibraryCard())
                    .append(getPin(), builder.getPin())
                    .append(getId(), builder.getId())
                    .append(getEmail(), builder.getEmail())
                    .append(getArenaUserId(), builder.getArenaUserId())
                    .append(getName(), builder.getName())
                    .append(getBirthDate(), builder.getBirthDate())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(getLibraryCard())
                    .append(getPin())
                    .append(getId())
                    .append(getEmail())
                    .append(getArenaUserId())
                    .append(getName())
                    .append(getBirthDate())
                    .toHashCode();
        }
    }
}
