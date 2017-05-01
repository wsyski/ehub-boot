package com.axiell.ehub.lms.palma;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.auth.Patron;

interface IResponseStatusChecker {
    void checkResponseStatus(com.axiell.arena.services.palma.util.status.Status status, EhubConsumer ehubConsumer, Patron patron);

    void checkResponseStatus(com.axiell.arena.services.palma.util.v267.status.Status status, EhubConsumer ehubConsumer);
}
