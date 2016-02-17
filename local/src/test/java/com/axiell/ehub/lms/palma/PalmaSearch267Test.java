package com.axiell.ehub.lms.palma;


import com.axiell.ehub.DevelopmentData;

import org.junit.Test;

import javax.xml.ws.Endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PalmaSearch267Test extends AbstractPalma {

    @Override
    Endpoint makeEndpoint(String palmaUrl) {
        return Endpoint.publish(palmaUrl + "/v267/catalogue", getPalmaSearchServiceInstance());
    }

    @Test
    public void getMediaClass() {
        String mediaClass = whenGetMediaClassExecuted();
        thenExpectedMediaClassIsReturned(mediaClass);
    }


    private void thenExpectedMediaClassIsReturned(final String mediaClass) {
        assertNotNull(mediaClass);
        assertEquals("eAudio", mediaClass);
    }

    private String whenGetMediaClassExecuted() {
        return palmaDataAccessor.getMediaClass(ehubConsumer, DevelopmentData.CONTENT_PROVIDER_TEST_EP, DevelopmentData.TEST_EP_RECORD_0_ID);
    }


    protected PalmaSearchService_267 getPalmaSearchServiceInstance() {
        return new PalmaSearchService_267();
    }
}
