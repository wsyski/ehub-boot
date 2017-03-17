package com.axiell.ehub.security;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.patron.Patron;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axiell.ehub.security.AuthInfo.*;
import static com.axiell.ehub.util.EhubUrlCodec.decode;

class EhubAuthHeaderParser implements IAuthHeaderParser {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
    private static final int FIRST_GROUP = 1;
    private static final int SECOND_GROUP = 2;
    private final Map<String, String> authHeaderParams;

    EhubAuthHeaderParser(final String value) {
        validateInputIsNotNull(value, ErrorCause.MISSING_AUTHORIZATION_HEADER);
        authHeaderParams = parseAuthorizationHeader(value);
    }

    private Map<String, String> parseAuthorizationHeader(String value) {

        final Map<String, String> parameterMap = new HashMap<>();
        final String[] parameters = value.split("\\s*,\\s*");

        for (String parameter : parameters) {
            Matcher matcher = PARAMETER_PATTERN.matcher(parameter);
            putParameterToMap(parameterMap, matcher);
        }
        return parameterMap;
    }

    private void validateInputIsNotNull(Object input, ErrorCause errorCause) {
        if (input == null) {
            throw new UnauthorizedException(errorCause);
        }
    }

    private void putParameterToMap(final Map<String, String> parameterMap, Matcher matcher) {
        if (matcher.matches()) {
            final String name = decode(matcher.group(FIRST_GROUP));
            final String value = decode(matcher.group(SECOND_GROUP));
            parameterMap.put(name, value);
        }
    }

    public Long getEhubConsumerId() {
        final String ehubConsumerId = authHeaderParams.get(EHUB_CONSUMER_ID);
        validateInputIsNotNull(ehubConsumerId, ErrorCause.MISSING_EHUB_CONSUMER_ID);
        return Long.parseLong(ehubConsumerId);
    }

    private String getPatronId() {
        return authHeaderParams.get(EHUB_PATRON_ID);
    }


    private String getLibraryCard() {
        return authHeaderParams.get(EHUB_LIBRARY_CARD);
    }

    private String getPin() {
        return authHeaderParams.get(EHUB_PIN);
    }

    private String getEmail() {
        return authHeaderParams.get(EHUB_EMAIL);
    }


    public String getActualSignature() {
        final String base64EncodedSignature = authHeaderParams.get(EHUB_SIGNATURE);
        validateInputIsNotNull(base64EncodedSignature, ErrorCause.MISSING_SIGNATURE);
        return base64EncodedSignature;
    }

    @Override
    public Patron getPatron() {
        final String patronId = getPatronId();
        final String libraryCard = getLibraryCard();
        final String pin = getPin();
        final String email =getEmail();
        return new Patron.Builder(libraryCard, pin).id(patronId).email(email).build();
    }
}