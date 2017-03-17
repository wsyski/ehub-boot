package com.axiell.ehub.security;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.patron.Patron;
import org.springframework.beans.factory.annotation.Autowired;

import static com.axiell.ehub.security.AuthInfo.EHUB_SCHEME;

class AuthInfoResolver implements IAuthInfoResolver {

    @Autowired
    private IAuthInfoSecretKeyResolver authInfoSecretKeyResolver;

    @Override
    public AuthInfo resolve(String authorizationHeader) {
        final AuthorizationHeaderParser authorizationHeaderParser = new AuthorizationHeaderParser(authorizationHeader);
        final AuthorizationHeaderParts authorizationHeaderParts = authorizationHeaderParser.getAuthorizationHeaderParts();
        if (authorizationHeaderParts != null) {
            boolean isValidate = authInfoSecretKeyResolver.isValidate();
            IAuthHeaderParser authHeaderParser;
            if (EHUB_SCHEME.equalsIgnoreCase(authorizationHeaderParts.getScheme())) {
                authHeaderParser = new EhubAuthHeaderParser(authorizationHeaderParts.getParameters());
            } else {
                throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
            }
            final Long ehubConsumerId = authHeaderParser.getEhubConsumerId();
            if (ehubConsumerId == null) {
                throw new UnauthorizedException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
            }
            final String secretKey = authInfoSecretKeyResolver.getSecretKey(ehubConsumerId);
            final Patron patron = authHeaderParser.getPatron();
            if (isValidate) {
                final String actualSignature = ((EhubAuthHeaderParser)authHeaderParser).getActualSignature();
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
        } else {
            throw new UnauthorizedException(ErrorCause.MISSING_AUTHORIZATION_HEADER);
        }
    }
}
