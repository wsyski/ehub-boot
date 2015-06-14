package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OcdAllMediaIT extends AbstractOcdIT {
    private static final String EAUDIO = "eAudio";
    private static final String EBOOK = "eBook";
    private String contentProviderRecordId;
    private List<MediaDTO> allMedia;

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenContentProviderRecordId(EAUDIO);
        whenGetAllMedia();
        thenExpectedMediaType(EAUDIO);
    }

    @Test
    public void eEbook() throws IFinder.NotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenContentProviderRecordId(EBOOK);
        whenGetAllMedia();
        thenExpectedMediaType(EBOOK);
    }

    private void whenGetAllMedia() {
        allMedia = underTest.getAllMedia(contentProviderConsumer);
        //for(MediaDTO mediaDTO: allMedia) {
        //    System.out.println(mediaDTO.getMediaType()+" "+mediaDTO.getIsbn());
        //}
    }

    private void thenExpectedMediaType(final String mediaType) throws IFinder.NotFoundException {
        IMatcher<MediaDTO> matcher = new ContentProviderRecordIdMediaMatcher(contentProviderRecordId);
        MediaDTO media = new CollectionFinder<MediaDTO>().find(matcher, allMedia);
        assertEquals(mediaType, media.getMediaType());
    }

    private void givenContentProviderRecordId(final String mediaType) {
        if (EAUDIO.equals(mediaType)) {
            contentProviderRecordId = EAUDIO_ISBN;
        } else {
            contentProviderRecordId = EBOOK_ISBN;
        }
    }
}
