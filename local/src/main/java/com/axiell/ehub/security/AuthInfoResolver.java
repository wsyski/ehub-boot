package com.axiell.ehub.security;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.patron.Patron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class AuthInfoResolver implements IAuthInfoResolver {

    @Autowired
    private IConsumerBusinessController consumerBusinessController;

    @Override
    @Transactional(readOnly = true)
    public AuthInfo resolve(String authorizationHeader) {
        final AuthHeaderParser parser = new AuthHeaderParser(authorizationHeader);
        final Long ehubConsumerId = parser.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);

        final Patron patron = makePatron(parser);
        final String actualSignature = parser.getActualSignature();
        final String secretKey = ehubConsumer.getSecretKey();
        if (actualSignature==null) {
            throw new UnauthorizedException(ErrorCause.MISSING_SIGNATURE);
        }
        final Signature expectedSignature = new Signature(AuthInfo.getSignatureItems(ehubConsumerId, patron), secretKey);

        if (expectedSignature.isValid(actualSignature))
            return new AuthInfo(ehubConsumerId, patron, secretKey);
        else
            throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
    }

    private Patron makePatron(AuthHeaderParser parser) {
        final String patronId = parser.getPatronId();
        final String libraryCard = parser.getLibraryCard();
        final String pin = parser.getPin();
        final String email = parser.getEmail();
        return new Patron.Builder(libraryCard, pin).id(patronId).email(email).build();
    }
}
