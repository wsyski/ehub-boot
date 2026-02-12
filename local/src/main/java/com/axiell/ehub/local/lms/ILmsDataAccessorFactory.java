package com.axiell.ehub.local.lms;

import com.axiell.ehub.common.consumer.EhubConsumer;

public interface ILmsDataAccessorFactory {
    ILmsDataAccessor getLmsDataAccessor(EhubConsumer ehubConsumer);
}
