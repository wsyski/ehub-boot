package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.ws.Endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/axiell/ehub/invocation-context.xml")
public abstract class AbstractPalma<T> {
    private Endpoint endpoint;

    @Autowired
    protected PalmaDataAccessor palmaDataAccessor;
    protected EhubConsumer ehubConsumer;

    @Before
    public void setUp() throws Exception {
        ehubConsumer = DevelopmentData.createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);

        String palmaUrl = ehubConsumer.getProperties().get(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL);
        endpoint = makeEndpoint(palmaUrl);
        customSetUp();
    }

    abstract Endpoint makeEndpoint(String palmaUrl);

    void customSetUp() {
    }

    @After
    public void tearDown() {
        endpoint.stop();
    }
}
