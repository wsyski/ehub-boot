package com.axiell.ehub.provider.ocd;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OcdMediaIT extends AbstractOcdIT {
    private static final String EAUDIO = "eAudio";
    private static final String EBOOK = "eBook";
    private String contentProviderRecordId;
    private MediaDTO actualMedia;

    @Test
    public void eAudio() throws MediaNotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenAudioTitleIdAsContentProviderRecordId();
        whenFindMedia();
        thenActualMediaTypeIsEAudio();
    }

    private void givenAudioTitleIdAsContentProviderRecordId() {
        contentProviderRecordId = EAUDIO_ISBN;
    }

    private void whenFindMedia() throws MediaNotFoundException {
        List<MediaDTO> allMedia = underTest.getAllMedia(contentProviderConsumer);
        IMediaMatcher matcher = new ContentProviderRecordIdMediaMatcher(contentProviderRecordId);
        actualMedia = MediaFinder.find(matcher, allMedia);
    }

    private void thenActualMediaTypeIsEAudio() {
        assertEquals(EAUDIO, actualMedia.getMediaType());
    }

    @Test
    public void eBook() throws MediaNotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenBookTitleIdAsContentProviderRecordId();
        whenFindMedia();
        thenActualMediaTypeIsEBook();
    }

    private void givenBookTitleIdAsContentProviderRecordId() {
        contentProviderRecordId = EBOOK_ISBN;
    }

    private void thenActualMediaTypeIsEBook() {
        assertEquals(EBOOK, actualMedia.getMediaType());
    }
}
