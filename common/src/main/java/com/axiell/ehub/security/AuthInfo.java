/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static com.axiell.ehub.util.EhubUrlCodec.encode;

import java.text.MessageFormat;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;

/**
 * Carries eHUB authentication information.
 * 
 * TODO: decide if more info should be included in the signature, e.g. timestamp 
 * TODO: include realm in header
 */
public final class AuthInfo {
    public static final String EHUB_SCHEME = "eHUB";
    public static final String EHUB_REALM = "realm=\"Axiell eHUB\"";
    static final String EHUB_CONSUMER_ID = "ehub_consumer_id";
    static final String EHUB_LIBRARY_CARD = "ehub_library_card";
    static final String EHUB_PIN = "ehub_pin";
    static final String EHUB_SIGNATURE = "ehub_signature";

    private static final String CONSUMER_ID_LIBRARY_CARD_PIN_PATTERN = EHUB_SCHEME + " " + EHUB_CONSUMER_ID + "=\"{0}\", " + EHUB_LIBRARY_CARD + "=\"{1}\", "
            + EHUB_PIN + "=\"{2}\", " + EHUB_SIGNATURE + "=\"{3}\"";

    private static final String CONSUMER_ID_PATTERN = EHUB_SCHEME + " " + EHUB_CONSUMER_ID + "=\"{0}\", " + EHUB_SIGNATURE + "=\"{1}\"";

    private final Long ehubConsumerId;
    private final String libraryCard;
    private final String pin;
    private final String signature;

    /**
     * Constructs a new {@link AuthInfo}.
     * 
     * @param ehubConsumerId the ID of the {@link EhubConsumer}
     * @param libraryCard the library card of a user, can be <code>null</code>
     * @param pin the pin of the library card, can be <code>null</code>
     * @param signature a {@link Signature}
     * @throws UnauthorizedException if the ID of the {@link EhubConsumer} or the signature is <code>null</code>
     */
    AuthInfo(final Long ehubConsumerId, final String libraryCard, final String pin, final Signature signature) {
        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        if (signature == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
        }
        this.ehubConsumerId = ehubConsumerId;
        this.libraryCard = libraryCard;
        this.pin = pin;
        this.signature = signature.toString();
    }

    /**
     * Creates the Authorization header value.
     * 
     * <p>
     * This method is used by the RESTEasy client proxy framework when inserting the value of the Authorization header.
     * </p>
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String encodedLibraryCard = libraryCard == null ? null : encode(libraryCard);
        final String encodedPin = pin == null ? null : encode(pin);

        if (encodedLibraryCard == null) {
            return MessageFormat.format(CONSUMER_ID_PATTERN, ehubConsumerId.toString(), signature);
        } else {
            return MessageFormat.format(CONSUMER_ID_LIBRARY_CARD_PIN_PATTERN, ehubConsumerId.toString(), encodedLibraryCard, encodedPin, signature);
        }
    }

    /**
     * Returns the ID of the {@link EhubConsumer}.
     * 
     * @return the ID of the {@link EhubConsumer}
     */
    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    /**
     * Returns a library card identifying a user.
     * 
     * @return a library card
     * @throws UnauthorizedException if the library card is <code>null</code>, i.e. it wasn't included in the
     * Authorization header
     */
    public String getRequiredLibraryCard() {
        if (libraryCard == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_LIBRARY_CARD);
        }
        return libraryCard;
    }

    /**
     * Returns a library card identifying a user.
     *
     * @return  a library card, <code>null</code> if it wasn't included in the Authorization header
     */
    public String getOptionalLibraryCard() {
        return libraryCard;
    }

    /**
     * Returns the pin to the library card.
     * 
     * @return a library card pin
     * @throws UnauthorizedException if the pin is <code>null</code>, i.e. it wasn't included in the Authorization
     * header
     */
    public String getPin() {
        if (pin == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_PIN);
        }
        return pin;
    }

    /**
     * Provides the possibility to build a new instance of {@link AuthInfo}.
     */
    public static final class Builder {
        // Required parameters
        private final Long ehubConsumerId;
        private final String ehubConsumerSecretKey;

        // Optional parameters
        private String libraryCard;
        private String pin;

        /**
         * Constructs a new {@link Builder}.
         * 
         * @param ehubConsumerId the non-null ID of the {@link EhubConsumer}
         * @param ehubConsumerSecretKey the non-null secret key of the {@link EhubConsumer}
         */
        public Builder(final Long ehubConsumerId, final String ehubConsumerSecretKey) {
            this.ehubConsumerId = ehubConsumerId;
            this.ehubConsumerSecretKey = ehubConsumerSecretKey;
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
         * @throws EhubException if the previously provided ID or secret key of the {@link EhubConsumer} is
         * <code>null</code>
         */
        public AuthInfo build() throws EhubException {
            if (ehubConsumerId == null) {
                final EhubError ehubError = ErrorCause.MISSING_EHUB_CONSUMER_ID.toEhubError();
                throw new EhubException(ehubError);
            }

            if (ehubConsumerSecretKey == null) {
                final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
                final EhubError ehubError = ErrorCause.MISSING_SECRET_KEY.toEhubError(ehubConsumerIdArg);
                throw new EhubException(ehubError);
            }

            final Signature signature = new Signature(ehubConsumerId, ehubConsumerSecretKey, libraryCard, pin);
            return new AuthInfo(ehubConsumerId, libraryCard, pin, signature);
        }
    }
}
