package com.axiell.ehub.provider;

import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition.STREAMING;
import static junit.framework.Assert.assertTrue;
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
    private IContent actualContent;

    @Before
    public void setUp() {
        underTest = new ContentFactory();
    }

    @Test
    public void createDownloadableContent() {
        givenDownloadableFormatDecoration();
        whenCreate();
        thenActualContentIsDownloadableContent();
    }

    @Test
    public void createStreamingContent() {
        givenStreamingFormatDecoration();
        whenCreate();
        thenActualContentIsStreamingContent();
    }

    private void givenDownloadableFormatDecoration() {
        given(formatDecoration.getContentDisposition()).willReturn(DOWNLOADABLE);
    }

    private void thenActualContentIsDownloadableContent() {
        assertTrue(actualContent instanceof DownloadableContent);
        DownloadableContent downloadableContent = (DownloadableContent) actualContent;
        assertEquals(URL, downloadableContent.getUrl());
    }

    private void givenStreamingFormatDecoration() {
        given(formatDecoration.getContentDisposition()).willReturn(STREAMING);
        given(formatDecoration.getPlayerWidth()).willReturn(WIDTH);
        given(formatDecoration.getPlayerHeight()).willReturn(HEIGHT);
    }

    private void whenCreate() {
        actualContent = underTest.create(URL, formatDecoration);
    }

    private void thenActualContentIsStreamingContent() {
        assertTrue(actualContent instanceof StreamingContent);
        StreamingContent streamingContent = (StreamingContent) actualContent;
        assertEquals(URL, streamingContent.getUrl());
        assertEquals(WIDTH, streamingContent.getWidth());
        assertEquals(HEIGHT, streamingContent.getHeight());
    }
}