package com.axiell.ehub.security;

import com.axiell.ehub.*;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;

public class AuthInfo {

    private final Long ehubConsumerId;
    private final Patron patron;

    public AuthInfo(final Long ehubConsumerId, final Patron patron) {
        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        this.ehubConsumerId = ehubConsumerId;
        this.patron = patron;
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    public Patron getPatron() {
        return patron;
    }

    /**
     * Provides the possibility to build a new instance of {@link AuthInfo}.
     */
    public static final class Builder {
        // Required parameters
        private final Long ehubConsumerId;

        // Optional parameters
        private String patronId;
        private String libraryCard;
        private String pin;
        private String email;

        /**
         * Constructs a new {@link Builder}.
         *
         * @param ehubConsumerId        the non-null ID of the {@link EhubConsumer}
         */
        public Builder(final Long ehubConsumerId) {
            this.ehubConsumerId = ehubConsumerId;
        }

        public Builder patronId(final String patronId) {
            this.patronId = patronId;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the library card to include in the {@link AuthInfo}.
         *
         * @param libraryCard the library card to set
         * @return this {@link Builder}
         */
        public Builder libraryCard(final String libraryCard) {
            this.libraryCard = libraryCard;
            return this;
        }

        /**
         * Sets the pin to include in the {@link AuthInfo}
         *
         * @param pin the pin to set
         * @return this {@link Builder}
         */
        public Builder pin(final String pin) {
            this.pin = pin;
            return this;
        }

        /**
         * Builds a new instance of {@link AuthInfo}.
         *
         * @return an {@link AuthInfo}
         */
        public AuthInfo build() {
            if (ehubConsumerId == null) {
                throw new InternalServerErrorException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
            }
            final Patron patron = new Patron.Builder(libraryCard, pin).id(patronId).email(email).build();
            return new AuthInfo(ehubConsumerId, patron);
        }
    }


}
