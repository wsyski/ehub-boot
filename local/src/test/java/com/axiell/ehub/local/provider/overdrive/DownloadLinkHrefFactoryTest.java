package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DownloadLinkHrefFactoryTest {

    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    private DownloadLinkTemplateDTO downloadLinkTemplate;
    private String errorPageUrl = "ErrorPageurl";
    private String readAuthUrl = "OdreadAuthUrl";
    private String expectedHref = "http://patron.api.overdrive.com/v1/patrons/me/checkouts/08F7D7E6-423F-45A6-9A1E-5AE9122C82E7/formats/ebook-overdrive/downloadlink?errorpageurl="
            + errorPageUrl + "&odreadauthurl=" + readAuthUrl;
    private String actualHref;

    @BeforeEach
    public void setUp() {
        downloadLinkTemplate = new DownloadLinkTemplateDTO("http://patron.api.overdrive.com/v1/patrons/me/checkouts/08F7D7E6-423F-45A6-9A1E-5AE9122C82E7/formats/ebook-overdrive/"
                + "downloadlink?errorpageurl={errorpageurl}&odreadauthurl={odreadauthurl}");
    }

    private void givenErrorPageUrl() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_ERROR_PAGE_URL)).willReturn(errorPageUrl);
    }

    private void givenOverDriveAuthUrl() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_READ_AUTH_URL)).willReturn(readAuthUrl);
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
        Assertions.assertEquals(expectedHref, actualHref);
    }
}
