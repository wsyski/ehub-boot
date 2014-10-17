package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.v267.patron.AuthenticatePatron;
import com.axiell.arena.services.palma.v267.patron.Patron;
import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse.AuthenticatePatronResult;
import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER;

@Component
class PatronFacade implements IPatronFacade {
    @Autowired
    private IPatronPortFactory patronPortFactory;

    @Override
    public AuthenticatePatronResult authenticatePatron(EhubConsumer ehubConsumer, String libraryCard, String pin) {
        AuthenticatePatron.AuthenticatePatronParam patronParam = createAuthenticatePatronParam(ehubConsumer, libraryCard, pin);
        Patron patron = patronPortFactory.getInstance(ehubConsumer);
        return patron.authenticatePatron(patronParam);
    }

    private AuthenticatePatron.AuthenticatePatronParam createAuthenticatePatronParam(EhubConsumer ehubConsumer, String libraryCard, String pin) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        AuthenticatePatron.AuthenticatePatronParam patronParam = new AuthenticatePatron.AuthenticatePatronParam();
        patronParam.setArenaMember(agencyMemberIdentifier);
        patronParam.setUser(libraryCard);
        patronParam.setPassword(pin);
        return patronParam;
    }
}
