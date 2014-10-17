package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;

interface IResponseStatusChecker {

    void checkResponseStatus(final Status status, final EhubConsumer ehubConsumer, final Patron patron);

    void check267ResponseStatus(com.axiell.arena.services.palma.util.v267.status.Status status, EhubConsumer ehubConsumer, Patron.Builder patronBuilder);
}
