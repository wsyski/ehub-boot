package com.axiell.ehub.lms;

import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LmsDataAccessorFactory implements ILmsDataAccessorFactory {

    @Autowired
    private ILmsDataAccessor arenaLocalApiDataAccessor;

    public ILmsDataAccessor getLmsDataAccessor(final EhubConsumer ehubConsumer) {
        return arenaLocalApiDataAccessor;
    }
}
