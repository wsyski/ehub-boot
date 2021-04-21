package com.axiell.ehub.lms;

import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LmsDataAccessorFactory implements ILmsDataAccessorFactory {
    @Autowired
    @Qualifier("palmaDataAccessor")
    private ILmsDataAccessor palmaDataAccessor;

    @Autowired
    @Qualifier("arenaLocalApiDataAccessor")
    private ILmsDataAccessor arenaLocalApiDataAccessor;

    public ILmsDataAccessor getLmsDataAccessor(final EhubConsumer ehubConsumer) {
        String arenaLocalApiEndpoint = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT);
        return (arenaLocalApiEndpoint.contains("arena.pa.palma")) ? palmaDataAccessor : arenaLocalApiDataAccessor;
    }
}
