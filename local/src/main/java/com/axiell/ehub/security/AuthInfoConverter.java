/*
 * Copyright (c) 2012 Axiell Group AB.
 */
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

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;

/**
 * Provides the possibility to convert a string to an {@link AuthInfo} and an {@link AuthInfo} to a string.
 */
@Provider
final class AuthInfoConverter implements StringConverter<AuthInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInfoConverter.class);
    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");
    private static final Pattern NVP_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
    private static final int GROUP_NO_1 = 1;
    private static final int GROUP_NO_2 = 2;

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    /**
     * @see org.jboss.resteasy.spi.StringConverter#fromString(java.lang.String)
     */
    @Override
    public AuthInfo fromString(final String authorizationHeader) {
        final Map<String, String> authHeaderParams = parseAuthorizationHeader(authorizationHeader);
        final String consumerIdValue = authHeaderParams.get(EHUB_CONSUMER_ID);
        final Long ehubConsumerId = consumerIdValue == null ? null : Long.valueOf(consumerIdValue);

        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }

        final String libraryCard = authHeaderParams.get(EHUB_LIBRARY_CARD);
        final String pin = authHeaderParams.get(EHUB_PIN);
        final String base64EncodedSignature = authHeaderParams.get(EHUB_SIGNATURE);

        if (base64EncodedSignature == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
        }

        final Signature actualSignature = new Signature(base64EncodedSignature);
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        
        if (ehubConsumer == null) {
            final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
            throw new UnauthorizedException(ErrorCause.EHUB_CONSUMER_NOT_FOUND, ehubConsumerIdArg);
        }
        
        final String secretKey = ehubConsumer.getSecretKey();
        
        if (secretKey == null) {
            final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
            throw new UnauthorizedException(ErrorCause.MISSING_SECRET_KEY, ehubConsumerIdArg);
        }
        
        final Signature expectedSignature = new Signature(ehubConsumerId, secretKey, libraryCard, pin);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Expected signature = '" + expectedSignature.toString() + "'");
            LOGGER.debug("Actual signature   = '" + actualSignature.toString() + "'");
        }

        if (actualSignature.isValid(expectedSignature)) {
            return new AuthInfo(ehubConsumerId, libraryCard, pin, actualSignature);
        } else {
            throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
        }
    }

    /**
     * @see org.jboss.resteasy.spi.StringConverter#toString(java.lang.Object)
     */
    @Override
    public String toString(final AuthInfo authInfo) {
        return authInfo.toString();
    }

    /**
     * Parses the provided authorization header.
     * 
     * @param authorizationHeader the authorization header to parse
     * @return a {@link Map} where the key is the name of the parameter and the value is the value of the parameter
     */
    private Map<String, String> parseAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
        }
        
        final Map<String, String> nvpMap = new HashMap<>();
        Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorizationHeader);

        if (matcher.matches()) {
            final String actScheme = matcher.group(GROUP_NO_1);

            if (EHUB_SCHEME.equalsIgnoreCase(actScheme)) {
                final String ehubAuthParams = matcher.group(GROUP_NO_2);
                final String[] nvps = ehubAuthParams.split("\\s*,\\s*");

                for (String nvp : nvps) {
                    matcher = NVP_PATTERN.matcher(nvp);

                    if (matcher.matches()) {
                        final String name = decode(matcher.group(GROUP_NO_1));
                        final String value = decode(matcher.group(GROUP_NO_2));
                        nvpMap.put(name, value);
                    }
                }
            }
        }
        return nvpMap;
    }
}
