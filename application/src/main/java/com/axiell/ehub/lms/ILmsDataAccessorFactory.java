package com.axiell.ehub.lms;

import com.axiell.ehub.consumer.EhubConsumer;

public interface ILmsDataAccessorFactory {
    ILmsDataAccessor getLmsDataAccessor(EhubConsumer ehubConsumer);
}
