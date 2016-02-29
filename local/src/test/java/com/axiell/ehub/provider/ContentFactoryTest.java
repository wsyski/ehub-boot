package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static com.axiell.ehub.provider.record.format.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.ContentDisposition.STREAMING;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ContentFactoryTest {
    private static final String URL = "url";
    private static final int WIDTH = 100;
    private static final int HEIGHT = 200;
    private ContentFactory underTest;
    @Mock
    private FormatDecoration formatDecoration;
    private ContentLink actualContentLink;

    @Before
    public void setUp() {
        underTest = new ContentFactory();
    }

    @Test
    public void create() {
        givenDownloadableFormatDecoration();
        whenCreate();
        thenActualContentLinkEqualsExpected();
    }

    private void givenDownloadableFormatDecoration() {
        given(formatDecoration.getContentDisposition()).willReturn(DOWNLOADABLE);
    }

    private void thenActualContentLinkEqualsExpected() {
        assertEquals(URL, actualContentLink.href());
    }

    private void givenStreamingFormatDecoration() {
        given(formatDecoration.getContentDisposition()).willReturn(STREAMING);
        given(formatDecoration.getPlayerWidth()).willReturn(WIDTH);
        given(formatDecoration.getPlayerHeight()).willReturn(HEIGHT);
    }

    private void whenCreate() {
        actualContentLink = underTest.create(Collections.singletonList(URL), formatDecoration).getContentLinks().get(0);
    }
}