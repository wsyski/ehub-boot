package com.axiell.ehub.lms.palma;

import com.axiell.ehub.consumer.EhubConsumer;

interface IPalmaFacadeFactory {
    IPalmaFacade getInstance(EhubConsumer ehubConsumer);
}
