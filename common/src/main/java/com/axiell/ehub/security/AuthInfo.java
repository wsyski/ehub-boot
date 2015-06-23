/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.axiell.ehub.util.EhubUrlCodec.encode;

/**
 * Carries eHUB authentication information.
 * <p/>
 * TODO: decide if more info should be included in the signature, e.g. timestamp
 * TODO: include realm in header
 */
public class AuthInfo {
    public static final String EHUB_SCHEME = "eHUB";
    public static final String EHUB_REALM = "realm=\"Axiell eHUB\"";
    static final String EHUB_CONSUMER_ID = "ehub_consumer_id";
    static final String EHUB_PATRON_ID = "ehub_patron_id";
    static final String EHUB_LIBRARY_CARD = "ehub_library_card";
    static final String EHUB_PIN = "ehub_pin";
    static final String EHUB_SIGNATURE = "ehub_signature";

    private static final String CONSUMER_ID_PATRON_ID_LIBRARY_CARD_PIN_PATTERN = EHUB_SCHEME + " " + EHUB_CONSUMER_ID + "=\"{0}\", " + EHUB_PATRON_ID + "=\"{1}\", " + EHUB_LIBRARY_CARD + "=\"{2}\", "
            + EHUB_PIN + "=\"{3}\", " + EHUB_SIGNATURE + "=\"{4}\"";

    private static final String CONSUMER_ID_LIBRARY_CARD_PIN_PATTERN = EHUB_SCHEME + " " + EHUB_CONSUMER_ID + "=\"{0}\", " + EHUB_LIBRARY_CARD + "=\"{1}\", "
            + EHUB_PIN + "=\"{2}\", " + EHUB_SIGNATURE + "=\"{3}\"";

    private static final String CONSUMER_ID_PATTERN = EHUB_SCHEME + " " + EHUB_CONSUMER_ID + "=\"{0}\", " + EHUB_SIGNATURE + "=\"{1}\"";

    private final Long ehubConsumerId;
    private final Patron patron;
    private final String signature;

    public AuthInfo(final Long ehubConsumerId, final Patron patron, final Signature signature) {
        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        if (signature == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
        }
        this.ehubConsumerId = ehubConsumerId;
        this.patron = patron;
        this.signature = signature.toString();
    }

    /**
     * Creates the Authorization header value.
     * <p/>
     * <p>
     * This method is used by the RESTEasy client proxy framework when inserting the value of the Authorization header.
     * </p>
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String encodedPatronId = patron.hasId() ? encode(patron.getId()) : null;
        final String encodedLibraryCard = patron.hasLibraryCard() ? encode(patron.getLibraryCard()) : null;
        final String pin = patron.getPin();
        final String encodedPin = pin == null ? null : encode(pin);

        if (patron.hasId())
            return MessageFormat.format(CONSUMER_ID_PATRON_ID_LIBRARY_CARD_PIN_PATTERN, ehubConsumerId.toString(), encodedPatronId, encodedLibraryCard, encodedPin, signature);
        else if (patron.hasLibraryCard())
            return MessageFormat.format(CONSUMER_ID_LIBRARY_CARD_PIN_PATTERN, ehubConsumerId.toString(), encodedLibraryCard, encodedPin, signature);
        else
            return MessageFormat.format(CONSUMER_ID_PATTERN, ehubConsumerId.toString(), signature);
    }

    /**
     * Returns the ID of the {@link EhubConsumer}.
     *
     * @return the ID of the {@link EhubConsumer}
     */
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
        private final String ehubConsumerSecretKey;

        // Optional parameters
        private String patronId;
        private String libraryCard;
        private String pin;

        /**
         * Constructs a new {@link Builder}.
         *
         * @param ehubConsumerId        the non-null ID of the {@link EhubConsumer}
         * @param ehubConsumerSecretKey the non-null secret key of the {@link EhubConsumer}
         */
        public Builder(final Long ehubConsumerId, final String ehubConsumerSecretKey) {
            this.ehubConsumerId = ehubConsumerId;
            this.ehubConsumerSecretKey = ehubConsumerSecretKey;
        }

        public Builder patronId(final String patronId) {
            this.patronId = patronId;
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
         * @throws EhubException if the previously provided ID or secret key of the {@link EhubConsumer} is
         *                       <code>null</code>
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

            final Patron patron = new Patron.Builder(libraryCard, pin).id(patronId).build();
            final Signature signature = new Signature(getSignatureItems(ehubConsumerId,patron), ehubConsumerSecretKey);
            return new AuthInfo(ehubConsumerId, patron, signature);
        }
    }

    public static List<?> getSignatureItems(final long ehubConsumerId, final Patron patron) {
        List<Object> signatureItems=new ArrayList<>();
        signatureItems.add(ehubConsumerId);
        if (patron.hasLibraryCard()) {
            signatureItems.add(patron.getLibraryCard());
            signatureItems.add(patron.getPin());
        }
        return signatureItems;
    }
}
