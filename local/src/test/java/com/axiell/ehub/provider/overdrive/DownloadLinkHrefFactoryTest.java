package com.axiell.ehub.provider.overdrive;

import static org.mockito.BDDMockito.given;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;

@RunWith(MockitoJUnitRunner.class)
public class DownloadLinkHrefFactoryTest {

    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    private String errorPageUrl = "ErrorPageurl";
    private String readAuthUrl = "OdreadAuthUrl";
    private String expectedHref = "http://patron.api.overdrive.com/v1/patrons/me/checkouts/08F7D7E6-423F-45A6-9A1E-5AE9122C82E7/formats/ebook-overdrive/downloadlink?errorpageurl="
	    + errorPageUrl + "&odreadauthurl=" + readAuthUrl;
    private String actualHref;

    @Before
    public void setUp() {
	downloadLinkTemplate = new DownloadLinkTemplateDTO();
	downloadLinkTemplate.setHref("http://patron.api.overdrive.com/v1/patrons/me/checkouts/08F7D7E6-423F-45A6-9A1E-5AE9122C82E7/formats/ebook-overdrive/"
		+ "downloadlink?errorpageurl={errorpageurl}&odreadauthurl={odreadauthurl}");
    }

    private void givenErrorPageUrl() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_ERROR_PAGE_URL)).willReturn(errorPageUrl);
    }

    private void givenOverDriveAuthUrl() {
	given(contentProviderConsumer.getProperty(ContentProviderConsumerPropertyKey.OVERDRIVE_READ_AUTH_URL)).willReturn(readAuthUrl);
    }

    @Test
    public void create() {
	givenErrorPageUrl();
	givenOverDriveAuthUrl();
	whenCreate();
	thenActualHrefEqualsExpectedHref();
    }

    private void whenCreate() {
	actualHref = DownloadLinkHrefFactory.create(contentProviderConsumer, downloadLinkTemplate);
    }

    private void thenActualHrefEqualsExpectedHref() {
	Assert.assertEquals(expectedHref, actualHref);
    }
}
