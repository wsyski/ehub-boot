package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.v267.patron.Patron;
import com.axiell.ehub.consumer.EhubConsumer;

interface IPatronPortFactory {

    Patron getInstance(EhubConsumer ehubConsumer);
}
