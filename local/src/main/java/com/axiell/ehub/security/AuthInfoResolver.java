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
    private static Logger LOGGER = LoggerFactory.getLogger(AuthInfoResolver.class);

    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController;

    @Override
    @Transactional(readOnly = true)
    public AuthInfo resolve(String authorizationHeader) {
        final AuthHeaderParser parser = new AuthHeaderParser(authorizationHeader);
        final Long ehubConsumerId = parser.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);

        final Patron patron = makePatron(parser);
        final Signature actualSignature = parser.getActualSignature();
        final Signature expectedSignature = new Signature(ehubConsumerId, ehubConsumer.getSecretKey(), patron);

        if (actualSignature.isValid(expectedSignature))
            return new AuthInfo(ehubConsumerId, patron, actualSignature);
        else
            throw new UnauthorizedException(ErrorCause.INVALID_SIGNATURE);
    }

    private Patron makePatron(AuthHeaderParser parser) {
        final String patronId = parser.getPatronId();
        final String libraryCard = parser.getLibraryCard();
        final String pin = parser.getPin();
        return new Patron.Builder(libraryCard, pin).id(patronId).build();
    }
}
