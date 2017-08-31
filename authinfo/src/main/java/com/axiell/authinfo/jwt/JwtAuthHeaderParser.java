package com.axiell.authinfo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.axiell.authinfo.*;
import org.springframework.beans.factory.annotation.Required;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class JwtAuthHeaderParser implements IAuthHeaderParser {
    private static final String PUBLIC_CLAIM_EMAIL = "email";
    private static final String PUBLIC_CLAIM_NAME = "name";
    private static final String PRIVATE_CLAIM_PATRON_ID = "patronId";
    private static final String PRIVATE_CLAIM_ARENA_USER_ID = "arenaUserId";
    private static final String PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID = "arenaAgencyMemberId";
    private static final String PRIVATE_CLAIM_LIBRARY_CARD = "libraryCard";
    private static final String PRIVATE_CLAIM_PIN = "pin";
    private static final String PRIVATE_CLAIM_EHUB_CONSUMER_ID = "ehubConsumerId";

    private IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver;

    @Override
    public AuthInfo parse(final String value) {
        DecodedJWT decodedJWT = JWT.decode(value);
        Long ehubConsumerId = getClaimAsLong(decodedJWT, PRIVATE_CLAIM_EHUB_CONSUMER_ID);
        Long arenaAgencyMemberId = getClaimAsLong(decodedJWT, PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID);
        String siteId = decodedJWT.getIssuer();
        Patron.Builder patronBuilder = new Patron.Builder()
                .email(getClaimAsString(decodedJWT, PUBLIC_CLAIM_EMAIL))
                .name(getClaimAsString(decodedJWT, PUBLIC_CLAIM_NAME))
                .libraryCard(getClaimAsString(decodedJWT, PRIVATE_CLAIM_LIBRARY_CARD))
                .pin(getClaimAsString(decodedJWT, PRIVATE_CLAIM_PIN))
                .id(getClaimAsString(decodedJWT, PRIVATE_CLAIM_PATRON_ID))
                .arenaUserId(getClaimAsLong(decodedJWT, PRIVATE_CLAIM_ARENA_USER_ID));
        AuthInfo.Builder authInfoBuilder = new AuthInfo.Builder()
                .arenaAgencyMemberId(arenaAgencyMemberId)
                .siteId(siteId)
                .ehubConsumerId(ehubConsumerId)
                .patron(patronBuilder.build());
        AuthInfo authInfo = authInfoBuilder.build();
        if (authHeaderSecretKeyResolver.isValidate()) {
            String secretKey = getSecretKey(authInfo);
            JWTVerifier jwtVerifier;
            try {
                jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
                jwtVerifier.verify(value);
            } catch (UnsupportedEncodingException ex) {
                throw new AuthInfoRuntimeException(ex.getMessage(), ex);
            } catch (JWTVerificationException ex) {
                throw new InvalidAuthorizationHeaderSignatureRuntimeException(ex);
            }
            jwtVerifier.verify(value);
        }
        return authInfo;
    }

    @Override
    public String serialize(final AuthInfo authInfo) {
        JWTCreator.Builder tokenBuilder = JWT.create().withIssuedAt(new Date()).withIssuer(authInfo.getSiteId());
        Date expirationDate = getExpirationDate(authInfo);
        if (expirationDate != null) {
            tokenBuilder = tokenBuilder.withExpiresAt(expirationDate);
        }
        if (authInfo.getEhubConsumerId() != null) {
            tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_EHUB_CONSUMER_ID, authInfo.getEhubConsumerId().intValue());
        }
        if (authInfo.getArenaAgencyMemberId() != null) {
            tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_ARENA_AGENCY_MEMBER_ID, authInfo.getArenaAgencyMemberId().intValue());
        }
        Patron patron = authInfo.getPatron();
        if (patron != null) {
            if (patron.hasEmail()) {
                tokenBuilder = tokenBuilder.withClaim(PUBLIC_CLAIM_EMAIL, patron.getEmail());
            }
            if (patron.hasName()) {
                tokenBuilder = tokenBuilder.withClaim(PUBLIC_CLAIM_NAME, patron.getName());
            }
            if (patron.hasLibraryCard()) {
                tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_LIBRARY_CARD, patron.getLibraryCard());
            }
            if (patron.hasPin()) {
                tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_PIN, patron.getPin());
            }
            if (patron.hasId()) {
                tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_PATRON_ID, patron.getId());
            }
            if (patron.getArenaUserId() != null) {
                tokenBuilder = tokenBuilder.withClaim(PRIVATE_CLAIM_ARENA_USER_ID, patron.getArenaUserId().intValue());
            }
        }
        String secretKey = getSecretKey(authInfo);
        try {
            return tokenBuilder.sign(Algorithm.HMAC256(secretKey));
        } catch (UnsupportedEncodingException ex) {
            throw new AuthInfoRuntimeException(ex.getMessage(), ex);
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

    public Date getExpirationDate(final AuthInfo authInfo) {
        if (authHeaderSecretKeyResolver.getExpirationTimeInSeconds(authInfo) > 0) {
            return new Date(new Date().getTime() + authHeaderSecretKeyResolver.getExpirationTimeInSeconds(authInfo) * 1000);
        } else {
            return null;
        }
    }

    public String getSecretKey(final AuthInfo authInfo) {
        return new String(Base64.getDecoder().decode(authHeaderSecretKeyResolver.getSecretKey(authInfo)), StandardCharsets.UTF_8);
    }

    @Required
    public void setAuthHeaderSecretKeyResolver(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
    }
}
