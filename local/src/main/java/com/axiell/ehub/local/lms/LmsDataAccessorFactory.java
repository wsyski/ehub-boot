package com.axiell.ehub.local.lms;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LmsDataAccessorFactory implements ILmsDataAccessorFactory {

    @Autowired
    private ILmsDataAccessor arenaLocalApiDataAccessor;

    public ILmsDataAccessor getLmsDataAccessor(final EhubConsumer ehubConsumer) {
        return arenaLocalApiDataAccessor;
    }
}
