package com.axiell.ehub.security;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.patron.Patron;
import org.springframework.beans.factory.annotation.Autowired;

class AuthInfoResolver implements IAuthInfoResolver {

    @Autowired
    private IAuthInfoSecretKeyResolver authInfoSecretKeyResolver;

    @Override
    public AuthInfo resolve(String authorizationHeader) {
        final AuthHeaderParser parser = new AuthHeaderParser(authorizationHeader);
        final Long ehubConsumerId = parser.getEhubConsumerId();
        if (ehubConsumerId == null) {
            throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        boolean isValidate = authInfoSecretKeyResolver.isValidate();
        final String secretKey = authInfoSecretKeyResolver.getSecretKey(ehubConsumerId);
        final Patron patron = makePatron(parser);
        if (isValidate) {
            final String actualSignature = parser.getActualSignature();
            if (actualSignature == null) {
                throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
            }
            final Signature expectedSignature = new Signature(AuthInfo.getSignatureItems(ehubConsumerId, patron), secretKey);

            //TODO: Remove when all Arena installations are upgraded
            final Signature expectedCompatibilitySignature = new Signature(AuthInfo.getSignatureCompatibilityItems(ehubConsumerId, patron), secretKey);

            if (!expectedSignature.isValid(actualSignature) && !expectedCompatibilitySignature.isValid(actualSignature)) {
                throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
            }
        }
        return new AuthInfo(ehubConsumerId, patron, secretKey);
    }

    private Patron makePatron(AuthHeaderParser parser) {
        final String patronId = parser.getPatronId();
        final String libraryCard = parser.getLibraryCard();
        final String pin = parser.getPin();
        final String email = parser.getEmail();
        return new Patron.Builder(libraryCard, pin).id(patronId).email(email).build();
    }
}
