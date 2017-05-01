package com.axiell.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.axiell.auth.AuthInfo;
import com.axiell.auth.IAuthHeaderParser;
import com.axiell.auth.IAuthHeaderSecretKeyResolver;
import com.axiell.auth.Patron;
import com.axiell.auth.util.AuthRuntimeException;
import org.springframework.beans.factory.annotation.Required;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class JwtAuthHeaderParser implements IAuthHeaderParser {
    private static final String PUBLIC_CLAIM_EMAIL = "email";
    private static final String PUBLIC_CLAIM_NAME = "name";
    private static final String PRIVATE_CLAIM_PATRON_ID = "patronId";
    private static final String PRIVATE_CLAIM_ARENA_USER_ID = "arenaUserId";
    private static final String PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID = "arenaAgencyMemberId";
    private static final String PRIVATE_CLAIM_ARENA_PORTAL_SITE_ID = "arenaPortalSiteId";
    private static final String PRIVATE_CLAIM_LIBRARY_CARD = "libraryCard";
    private static final String PRIVATE_CLAIM_PIN = "pin";
    private static final String PRIVATE_CLAIM_EHUB_CONSUMER_ID = "ehubConsumerId";

    private IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver;

    @Override
    public AuthInfo parse(final String value) {
        DecodedJWT decodedJWT = JWT.decode(value);
        Long ehubConsumerId = getClaimAsLong(decodedJWT, PRIVATE_CLAIM_EHUB_CONSUMER_ID);
        Long arenaAgencyMemberId = getClaimAsLong(decodedJWT, PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID);
        Long arenaPortalSiteId = getClaimAsLong(decodedJWT, PRIVATE_CLAIM_ARENA_PORTAL_SITE_ID);
        Patron.Builder patronBuilder = new Patron.Builder()
                .email(getClaimAsString(decodedJWT, PUBLIC_CLAIM_EMAIL))
                .name(getClaimAsString(decodedJWT, PUBLIC_CLAIM_NAME))
                .libraryCard(getClaimAsString(decodedJWT, PRIVATE_CLAIM_LIBRARY_CARD))
                .pin(getClaimAsString(decodedJWT, PRIVATE_CLAIM_PIN))
                .id(getClaimAsString(decodedJWT, PRIVATE_CLAIM_PATRON_ID))
                .arenaUserId(getClaimAsLong(decodedJWT, PRIVATE_CLAIM_ARENA_USER_ID));
        AuthInfo.Builder authInfoBuilder = new AuthInfo.Builder()
                .arenaAgencyMemberId(arenaAgencyMemberId)
                .arenaPortalSiteId(arenaPortalSiteId)
                .ehubConsumerId(ehubConsumerId)
                .patron(patronBuilder.build());
        AuthInfo authInfo = authInfoBuilder.build();
        if (authHeaderSecretKeyResolver.isValidate()) {
            String secretKey = authHeaderSecretKeyResolver.getSecretKey(authInfo.getEhubConsumerId());
            JWTVerifier jwtVerifier;
            try {
                jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            } catch (UnsupportedEncodingException ex) {
                throw new AuthRuntimeException(ex.getMessage(), ex);
            }
            jwtVerifier.verify(value);
        }
        return authInfo;
    }

    @Override
    public String serialize(final AuthInfo authInfo) {
        JWTCreator.Builder tokenBuilder = JWT.create().withIssuedAt(new Date());
        Date expirationDate = getExpirationDate();
        if (expirationDate != null) {
            tokenBuilder = tokenBuilder.withExpiresAt(expirationDate);
        }
        tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_EHUB_CONSUMER_ID, authInfo.getEhubConsumerId());
        tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID, authInfo.getArenaAgencyMemberId());
        tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_ARENA_PORTAL_SITE_ID, authInfo.getArenaPortalSiteId());
        Patron patron = authInfo.getPatron();
        if (patron != null) {
            tokenBuilder = withClaim(tokenBuilder, PUBLIC_CLAIM_EMAIL, patron.getEmail());
            tokenBuilder = withClaim(tokenBuilder, PUBLIC_CLAIM_NAME, patron.getName());
            tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_LIBRARY_CARD, patron.getLibraryCard());
            tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_PIN, patron.getPin());
            tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_PATRON_ID, patron.getId());
            tokenBuilder = withClaim(tokenBuilder, PRIVATE_CLAIM_ARENA_USER_ID, patron.getArenaUserId());
        }
        String secretKey = authHeaderSecretKeyResolver.getSecretKey(authInfo.getEhubConsumerId());
        try {
            return tokenBuilder.sign(Algorithm.HMAC256(secretKey));
        } catch (UnsupportedEncodingException ex) {
            throw new AuthRuntimeException(ex.getMessage(), ex);
        }
    }

    private static <T> JWTCreator.Builder withClaim(final JWTCreator.Builder builder, final String key, final T value) {
        if (value instanceof String) {
            return builder.withClaim(key, String.class.cast(value));
        } else if (value instanceof Long) {
            return builder.withClaim(key, (int) Long.class.cast(value).longValue());
        } else if (value instanceof Integer) {
            return builder.withClaim(key, Integer.class.cast(value));
        } else if (value instanceof Boolean) {
            return builder.withClaim(key, Boolean.class.cast(value));
        } else if (value instanceof Date) {
            return builder.withClaim(key, Date.class.cast(value));
        } else {
            return builder;
        }
    }

    private static Long getClaimAsLong(final DecodedJWT decodedJWT, final String key) {
        Claim claim = decodedJWT.getClaim(key);
        return (claim == null || claim.isNull()) ? null : new Long(claim.asInt());
    }

    private static String getClaimAsString(final DecodedJWT decodedJWT, final String key) {
        Claim claim = decodedJWT.getClaim(key);
        return (claim == null || claim.isNull()) ? null : claim.asString();
    }

    public Date getExpirationDate() {
        if (authHeaderSecretKeyResolver.getExpirationTimeInSeconds() > 0) {
            return new Date(new Date().getTime() + authHeaderSecretKeyResolver.getExpirationTimeInSeconds() * 1000);
        } else {
            return null;
        }
    }

    public String getSecretKey(final Long id) {
        return new String(Base64.getDecoder().decode(authHeaderSecretKeyResolver.getSecretKey(id)), StandardCharsets.UTF_8);
    }

    @Required
    public void setAuthHeaderSecretKeyResolver(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
    }
}
