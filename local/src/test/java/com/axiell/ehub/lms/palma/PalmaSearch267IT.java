package com.axiell.ehub.lms.palma;


import com.axiell.ehub.DevelopmentData;

import com.axiell.ehub.provider.ContentProvider;
import org.junit.Test;

import javax.xml.ws.Endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PalmaSearch267IT extends AbstractPalmaIT {

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
        return palmaDataAccessor.getMediaClass(ehubConsumer, ContentProvider.CONTENT_PROVIDER_ELIB, DevelopmentData.ELIB_RECORD_0_ID);
    }


    protected PalmaSearchService_267 getPalmaSearchServiceInstance() {
        return new PalmaSearchService_267();
    }
}
