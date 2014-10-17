package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse;
import com.axiell.ehub.consumer.EhubConsumer;

import static com.axiell.arena.services.palma.v267.patron.AuthenticatePatronResponse.AuthenticatePatronResult;

public interface IPatronFacade {

    AuthenticatePatronResult authenticatePatron(EhubConsumer ehubConsumer, String libraryCard, String pin);
}
