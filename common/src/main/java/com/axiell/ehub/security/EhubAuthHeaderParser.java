package com.axiell.ehub.security;

import com.axiell.auth.AuthInfo;
import com.axiell.auth.IAuthHeaderParser;
import com.axiell.auth.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.auth.Patron;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static com.axiell.ehub.util.EhubUrlCodec.decode;

public class EhubAuthHeaderParser implements IAuthHeaderParser {
    private static final String EHUB_CONSUMER_ID = "ehub_consumer_id";
    private static final String EHUB_PATRON_ID = "ehub_patron_id";
    private static final String EHUB_LIBRARY_CARD = "ehub_library_card";
    private static final String EHUB_PIN = "ehub_pin";
    private static final String EHUB_EMAIL = "ehub_email";
    private static final String EHUB_SIGNATURE = "ehub_signature";

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
    private static final int FIRST_GROUP = 1;
    private static final int SECOND_GROUP = 2;

    private IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver;
    private  boolean isValidateSignature;

    public AuthInfo parse(final String value) {
        validateInputIsNotNull(value, ErrorCause.MISSING_AUTHORIZATION_HEADER);
        final Map<String, String> authHeaderParams = parseAuthorizationHeader(value);
        long ehubConsumerId = getEhubConsumerId(authHeaderParams);
        Patron patron = getPatron(authHeaderParams);
        final String secretKey = authHeaderSecretKeyResolver.getSecretKey(ehubConsumerId);
        final String actualSignature = getActualSignature(authHeaderParams);
        if (actualSignature == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
        }

        if (isValidateSignature) {
            final Signature expectedSignature = new Signature(getSignatureItems(ehubConsumerId, patron), secretKey);

            //TODO: Remove when all Arena installations are upgraded
            final Signature expectedCompatibilitySignature = new Signature(getSignatureCompatibilityItems(ehubConsumerId, patron), secretKey);

            if (!expectedSignature.isValid(actualSignature) && !expectedCompatibilitySignature.isValid(actualSignature)) {
                throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
            }
        }
        return new AuthInfo.Builder().ehubConsumerId(ehubConsumerId).patron(patron).build();
    }

    @Override
    public String serialize(final AuthInfo authInfo) {
        final StringBuilder sb = new StringBuilder();
        Long ehubConsumerId = authInfo.getEhubConsumerId();
        if (ehubConsumerId == null) {
            throw new InternalServerErrorException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        final String secretKey = authHeaderSecretKeyResolver.getSecretKey(ehubConsumerId);
        if (secretKey == null) {
            final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumerId);
            throw new InternalServerErrorException(ErrorCause.MISSING_SECRET_KEY, ehubConsumerIdArg);
        }

        appendItem(sb, EHUB_CONSUMER_ID, String.valueOf(authInfo.getEhubConsumerId()));
        Patron patron = authInfo.getPatron();
        if (patron != null) {
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
        appendItem(sb, EHUB_SIGNATURE, getSignature(ehubConsumerId, patron, secretKey));
        return EHUB_SCHEME + " " + sb.toString();
    }

    private void appendItem(final StringBuilder sb, final String name, final String value) {
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(name);
        sb.append("=\"");
        sb.append(authInfoEncode(value));
        sb.append("\"");
    }

    private static String getSignature(final long ehubConsumerId, final Patron patron, final String secretKey) {
        Signature signature = new Signature(getSignatureItems(ehubConsumerId, patron), secretKey);
        return signature.toString();
    }

    private static Map<String, String> parseAuthorizationHeader(String value) {

        final Map<String, String> parameterMap = new HashMap<>();
        final String[] parameters = value.split("\\s*,\\s*");

        for (String parameter : parameters) {
            Matcher matcher = PARAMETER_PATTERN.matcher(parameter);
            putParameterToMap(parameterMap, matcher);
        }
        return parameterMap;
    }

    private static void validateInputIsNotNull(Object input, ErrorCause errorCause) {
        if (input == null) {
            throw new UnauthorizedException(errorCause);
        }
    }

    private static void putParameterToMap(final Map<String, String> parameterMap, Matcher matcher) {
        if (matcher.matches()) {
            final String name = decode(matcher.group(FIRST_GROUP));
            final String value = decode(matcher.group(SECOND_GROUP));
            parameterMap.put(name, value);
        }
    }

    private static long getEhubConsumerId(final Map<String, String> authHeaderParams) {
        final String ehubConsumerId = authHeaderParams.get(EHUB_CONSUMER_ID);
        validateInputIsNotNull(ehubConsumerId, ErrorCause.MISSING_EHUB_CONSUMER_ID);
        return Long.parseLong(ehubConsumerId);
    }

    private static String getPatronId(final Map<String, String> authHeaderParams) {
        return authHeaderParams.get(EHUB_PATRON_ID);
    }


    private static String getLibraryCard(final Map<String, String> authHeaderParams) {
        return authHeaderParams.get(EHUB_LIBRARY_CARD);
    }

    private static String getPin(final Map<String, String> authHeaderParams) {
        return authHeaderParams.get(EHUB_PIN);
    }

    private static String getEmail(final Map<String, String> authHeaderParams) {
        return authHeaderParams.get(EHUB_EMAIL);
    }


    private static String getActualSignature(final Map<String, String> authHeaderParams) {
        final String base64EncodedSignature = authHeaderParams.get(EHUB_SIGNATURE);
        validateInputIsNotNull(base64EncodedSignature, ErrorCause.MISSING_SIGNATURE);
        return base64EncodedSignature;
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

    private static Patron getPatron(final Map<String, String> authHeaderParams) {
        final String patronId = getPatronId(authHeaderParams);
        final String libraryCard = getLibraryCard(authHeaderParams);
        final String pin = getPin(authHeaderParams);
        final String email = getEmail(authHeaderParams);
        return new Patron.Builder().id(patronId).libraryCard(libraryCard).pin(pin).email(email).build();
    }

    @Required
    public void setAuthHeaderSecretKeyResolver(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
    }

    @Required
    public void setValidateSignature(boolean isValidateSignature) {
        this.isValidateSignature = isValidateSignature;
    }
}