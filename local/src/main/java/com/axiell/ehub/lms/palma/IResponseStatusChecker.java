package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;

interface IResponseStatusChecker {

    void checkResponseStatus(final Status status, final EhubConsumer ehubConsumer, final Patron patron);
}
