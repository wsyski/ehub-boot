package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.ArrayList;
import java.util.List;

import static com.axiell.ehub.util.EhubUrlCodec.encode;

public class AuthInfo {
    static final String EHUB_SCHEME = "eHUB";
    static final String REALM = "realm=\"Axiell eHUB\"";
    static final String EHUB_CONSUMER_ID = "ehub_consumer_id";
    static final String EHUB_PATRON_ID = "ehub_patron_id";
    static final String EHUB_LIBRARY_CARD = "ehub_library_card";
    static final String EHUB_PIN = "ehub_pin";
    static final String EHUB_EMAIL = "ehub_email";
    static final String EHUB_SIGNATURE = "ehub_signature";

    private final Long ehubConsumerId;
    private final Patron patron;
    private final String secretKey;

    public AuthInfo(final Long ehubConsumerId, final Patron patron, final String secretKey) {
        this.secretKey = secretKey;
        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        this.ehubConsumerId = ehubConsumerId;
        this.patron = patron;
    }

    private String getSignature() {
        Signature signature = new Signature(getSignatureItems(ehubConsumerId, patron), secretKey);
        return signature.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        appendItem(sb, EHUB_CONSUMER_ID, String.valueOf(ehubConsumerId));
        if (patron!=null) {
            if (patron.hasId()) {
                appendItem(sb, EHUB_PATRON_ID, patron.getId());
            }
            if (patron.hasLibraryCard()) {
                appendItem(sb, EHUB_LIBRARY_CARD, patron.getLibraryCard());
            }
            if (patron.hasPin()) {
                appendItem(sb, EHUB_PIN, patron.getPin());

            }
            if (patron.hasEmail()) {
                appendItem(sb, EHUB_EMAIL, patron.getEmail());
            }
        }
        appendItem(sb, EHUB_SIGNATURE, getSignature());
        return EHUB_SCHEME + " " + sb.toString();
    }

    private void appendItem(final StringBuilder sb, final String name, final String value) {
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(name);
        sb.append("=\"");
        sb.append(encode(value));
        sb.append("\"");
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
        private final String ehubConsumerSecretKey;

        // Optional parameters
        private String patronId;
        private String libraryCard;
        private String pin;
        private String email;

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

            final Patron patron = new Patron.Builder(libraryCard, pin).id(patronId).email(email).build();
            return new AuthInfo(ehubConsumerId, patron, ehubConsumerSecretKey);
        }
    }

    static List<String> getSignatureItems(final Long ehubConsumerId, final Patron patron) {
        List<String> signatureItems = new ArrayList<>();
        if (ehubConsumerId != null) {
            signatureItems.add(String.valueOf(ehubConsumerId));
        }
        if (patron != null) {
            if (patron.hasId()) {
                signatureItems.add(patron.getId());
            }
            if (patron.hasLibraryCard()) {
                signatureItems.add(patron.getLibraryCard());
            }
            if (patron.hasPin()) {
                signatureItems.add(patron.getPin());
            }
            if (patron.hasEmail()) {
                signatureItems.add(patron.getEmail());
            }
        }
        return signatureItems;
    }

    //TODO: Remove when all Arena installations are upgraded
    @Deprecated
    static List<String> getSignatureCompatibilityItems(final Long ehubConsumerId, final Patron patron) {
        List<String> signatureItems = new ArrayList<>();
        if (ehubConsumerId != null) {
            signatureItems.add(String.valueOf(ehubConsumerId));
        }
        if (patron != null) {
            if (patron.hasLibraryCard()) {
                signatureItems.add(patron.getLibraryCard());
            }
            if (patron.hasPin()) {
                signatureItems.add(patron.getPin());
            }
            if (patron.hasEmail()) {
                signatureItems.add(patron.getEmail());
            }
        }
        return signatureItems;
    }

}
