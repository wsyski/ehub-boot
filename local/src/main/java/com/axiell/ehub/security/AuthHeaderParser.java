package com.axiell.ehub.security;

import static com.axiell.ehub.security.AuthInfo.EHUB_CONSUMER_ID;
import static com.axiell.ehub.security.AuthInfo.EHUB_LIBRARY_CARD;
import static com.axiell.ehub.security.AuthInfo.EHUB_PIN;
import static com.axiell.ehub.security.AuthInfo.EHUB_SCHEME;
import static com.axiell.ehub.security.AuthInfo.EHUB_SIGNATURE;
import static com.axiell.ehub.util.EhubUrlCodec.decode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axiell.ehub.ErrorCause;

class AuthHeaderParser {
    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
    private static final int FIRST_GROUP = 1;
    private static final int SECOND_GROUP = 2;
    private final Map<String, String> authHeaderParams;

    AuthHeaderParser(String authorizationHeader) {
	validateInputIsNotNull(authorizationHeader, ErrorCause.MISSING_AUTHORIZATION_HEADER);
	authHeaderParams = parseAuthorizationHeader(authorizationHeader);
    }

    private Map<String, String> parseAuthorizationHeader(String authorizationHeader) {
	final Map<String, String> parameterMap = new HashMap<>();
	Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorizationHeader);

	if (isEhubScheme(matcher)) {
	    final String[] parameters = getAuthorizationParameters(matcher);

	    for (String parameter : parameters) {
		matcher = PARAMETER_PATTERN.matcher(parameter);
		putParameterToMap(parameterMap, matcher);
	    }
	}
	return parameterMap;
    }

    private void validateInputIsNotNull(Object input, ErrorCause errorCause) {
	if (input == null) {
	    throw new UnauthorizedException(errorCause);
	}
    }

    private boolean isEhubScheme(Matcher matcher) {
	if (matcher.matches()) {
	    final String actualScheme = matcher.group(FIRST_GROUP);
	    return EHUB_SCHEME.equalsIgnoreCase(actualScheme);
	}
	return false;
    }

    private String[] getAuthorizationParameters(Matcher matcher) {
	final String params = matcher.group(SECOND_GROUP);
	return params.split("\\s*,\\s*");
    }
    
    private void putParameterToMap(final Map<String, String> parameterMap, Matcher matcher) {
	if (matcher.matches()) {
	    final String name = decode(matcher.group(FIRST_GROUP));
	    final String value = decode(matcher.group(SECOND_GROUP));
	    parameterMap.put(name, value);
	}
    }

    Long getEhubConsumerId() {
	final String ehubConsumerId = authHeaderParams.get(EHUB_CONSUMER_ID);
	validateInputIsNotNull(ehubConsumerId, ErrorCause.MISSING_EHUB_CONSUMER_ID);
	return Long.valueOf(ehubConsumerId);
    }

    String getLibraryCard() {
	return authHeaderParams.get(EHUB_LIBRARY_CARD);
    }

    String getPin() {
	return authHeaderParams.get(EHUB_PIN);
    }

    Signature getActualSignature() {
	final String base64EncodedSignature = authHeaderParams.get(EHUB_SIGNATURE);
	validateInputIsNotNull(base64EncodedSignature, ErrorCause.MISSING_SIGNATURE);
	return new Signature(base64EncodedSignature);
    }
}